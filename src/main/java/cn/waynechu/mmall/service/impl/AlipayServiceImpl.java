package cn.waynechu.mmall.service.impl;

import cn.waynechu.mmall.common.Const;
import cn.waynechu.mmall.common.Result;
import cn.waynechu.mmall.entity.Order;
import cn.waynechu.mmall.entity.OrderItem;
import cn.waynechu.mmall.entity.PayInfo;
import cn.waynechu.mmall.mapper.OrderItemMapper;
import cn.waynechu.mmall.mapper.OrderMapper;
import cn.waynechu.mmall.mapper.PayInfoMapper;
import cn.waynechu.mmall.properties.AlipayProperties;
import cn.waynechu.mmall.properties.FtpServerProperties;
import cn.waynechu.mmall.service.AlipayService;
import cn.waynechu.mmall.service.FtpService;
import cn.waynechu.mmall.util.BigDecimalUtil;
import cn.waynechu.mmall.util.DateTimeUtil;
import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.builder.AlipayTradeQueryRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author waynechu
 * Created 2018-06-05 12:21
 */
@Slf4j
@Service
public class AlipayServiceImpl implements AlipayService {

    @Autowired
    private AlipayProperties alipayProperties;

    @Autowired
    private FtpService ftpService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private PayInfoMapper payInfoMapper;

    @Autowired
    private FtpServerProperties ftpServerProperties;

    private static AlipayTradeService tradeService;

    static {
        // 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
        // Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
        Configs.init("zfbinfo.properties");

        // 使用Configs提供的默认参数 AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
    }

    @Override
    public Result queryPayStatus(Long orderNo) {
        AlipayTradeQueryRequestBuilder builder = new AlipayTradeQueryRequestBuilder();
        builder.setOutTradeNo(orderNo.toString());
        AlipayF2FQueryResult alipayF2FQueryResult = tradeService.queryTradeResult(builder);
        if (alipayF2FQueryResult.isTradeSuccess()) {
            return Result.createBySuccess();
        }
        return Result.createByError();
    }

    @Override
    public Result pay(Long orderNo, Long userId, String path) {
        Map<String, String> resultMap = new HashMap<>();
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            return Result.createByErrorMessage("用户没有该订单");
        }
        resultMap.put("orderNo", String.valueOf(order.getOrderNo()));

        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成
        String outTradeNo = String.valueOf(order.getOrderNo());

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = "扫码支付，订单号：" + outTradeNo;

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = "订单号：" + outTradeNo + "，购买商品共" + totalAmount + "元";

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();

        List<OrderItem> orderItemList = orderItemMapper.listByOrderNoAndUserId(orderNo, userId);
        for (OrderItem orderItem : orderItemList) {
            // 创建商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
            GoodsDetail goods = GoodsDetail.newInstance(orderItem.getProductId().toString(), orderItem.getProductName(),
                    BigDecimalUtil.mul(orderItem.getCurrentUnitPrice().doubleValue(), 100D).longValue(), orderItem.getQuantity());
            // 创建好一个商品后添加至商品明细列表
            goodsDetailList.add(goods);
        }

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                // 支付宝服务器主动通知商户服务器里指定的页面http路径，根据需要设置
                .setNotifyUrl(alipayProperties.getAlipayCallbackUrl())
                .setGoodsDetailList(goodsDetailList);

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功 : )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                File folder = new File(path);
                if (!folder.exists()) {
                    folder.setWritable(true);
                    folder.mkdirs();
                }

                // 需要修改为运行机器上的路径
                String qrPath = String.format(path + "/qr-%s.png", response.getOutTradeNo());
                log.info("qrPath: " + qrPath);
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);

                String qrFileName = String.format("qr-%s.png", response.getOutTradeNo());
                File targetFile = new File(path, qrFileName);
                // 将targetFile上传到FTP服务器上
                ftpService.connectToFTP(ftpServerProperties.getIp(), ftpServerProperties.getUser(), ftpServerProperties.getPassword());
                // 上传到ftp目录下的img文件夹下，注意如果该文件夹不存在，FTP会返回550 Failed to change directory.错误
                ftpService.uploadFileToFTP(targetFile, "img", targetFile.getName());
                ftpService.disconnectFTP();

                String qrUrl = ftpServerProperties.getUrlPrefix() + targetFile.getName();
                resultMap.put("qrUrl", qrUrl);
                return Result.createBySuccess(resultMap);

            case FAILED:
                log.error("支付宝预下单失败!!!");
                return Result.createByErrorMessage("支付宝预下单失败!!!");

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                return Result.createByErrorMessage("系统异常，预下单状态未知!!!");

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                return Result.createByErrorMessage("不支持的交易状态，交易返回异常!!!");
        }
    }

    /**
     * 简单打印应答信息
     */
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }

    @Override
    public Result checkPayParams(Map<String, String> params) {
        // 商户订单号
        Long orderNo = Long.parseLong(params.get("out_trade_no"));
        // 支付宝交易号
        String tradeNo = params.get("trade_no");
        // 交易状态
        String tradeStatus = params.get("trade_status");
        // 实收金额
        BigDecimal receiptAmount = new BigDecimal(params.get("receipt_amount"));
        // 交易付款时间
        String gmtPayment = params.get("gmt_payment");

        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            return Result.createByErrorMessage("非mmall订单，忽略该回调");
        }
        if (order.getPayment().compareTo(receiptAmount) != 0) {
            return Result.createByErrorMessage("非mmall订单，忽略该回调");
        }
        if (order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()) {
            return Result.createBySuccessMessage("支付宝重复调用");
        }
        if (Const.AlipayCallback.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)) {
            order.setPaymentTime(DateTimeUtil.toLocalDateTimeFromString(gmtPayment));
            order.setStatus(Const.OrderStatusEnum.PAID.getCode());
            orderMapper.updateByPrimaryKeySelective(order);
        }

        PayInfo payInfo = new PayInfo();
        payInfo.setUserId(order.getUserId());
        payInfo.setOrderNo(order.getOrderNo());
        payInfo.setPayPlatform(Const.PayPlatformEnum.ALIPAY.getCode());
        payInfo.setPlatformNumber(tradeNo);
        payInfo.setPlatformStatus(tradeStatus);
        payInfoMapper.insert(payInfo);

        return Result.createBySuccess();
    }
}
