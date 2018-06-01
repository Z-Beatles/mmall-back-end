package cn.waynechu.mmall.web.portal;

import cn.waynechu.mmall.common.Const;
import cn.waynechu.mmall.common.ResultEnum;
import cn.waynechu.mmall.common.Result;
import cn.waynechu.mmall.service.OrderService;
import cn.waynechu.mmall.vo.UserInfoVO;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author waynechu
 * Created 2018-05-28 13:17
 */
@Slf4j
@Api(tags = "门户-订单接口")
@RestController
@RequestMapping("/v1/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create.do")
    public Result create(Long shippingId, HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return Result.createByErrorCodeMessage(ResultEnum.NEED_LOGIN.getCode(), ResultEnum.NEED_LOGIN.getMsg());
        }
        return orderService.createOrder(currentUser.getId(), shippingId);
    }

    @DeleteMapping("/cancel.do")
    public Result cancel(Long orderNo, HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return Result.createByErrorCodeMessage(ResultEnum.NEED_LOGIN.getCode(), ResultEnum.NEED_LOGIN.getMsg());
        }
        return orderService.cancel(currentUser.getId(), orderNo);
    }

    @GetMapping("/get_order_cart_product.do")
    public Result getOrderCartProduct(HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return Result.createByErrorCodeMessage(ResultEnum.NEED_LOGIN.getCode(), ResultEnum.NEED_LOGIN.getMsg());
        }
        return orderService.getOrderCartProduct(currentUser.getId());
    }

    @GetMapping("/detail.do")
    public Result detail(Long orderNo, HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return Result.createByErrorCodeMessage(ResultEnum.NEED_LOGIN.getCode(), ResultEnum.NEED_LOGIN.getMsg());
        }
        return orderService.getOrderDetail(currentUser.getId(), orderNo);
    }

    @GetMapping("/list.do")
    public Result list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                       @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                       HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return Result.createByErrorCodeMessage(ResultEnum.NEED_LOGIN.getCode(), ResultEnum.NEED_LOGIN.getMsg());
        }
        return orderService.getOrderList(currentUser.getId(), pageNum, pageSize);
    }


    @GetMapping("/pay.do")
    public Result pay(Long orderNo, HttpServletRequest request, HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return Result.createByErrorCodeMessage(ResultEnum.NEED_LOGIN.getCode(), ResultEnum.NEED_LOGIN.getMsg());
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        return orderService.pay(orderNo, currentUser.getId(), path);
    }

    @GetMapping("/query_order_pay_status.do")
    public Result<Boolean> queryOrderPayStatus(Long orderNo, HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return Result.createByErrorCodeMessage(ResultEnum.NEED_LOGIN.getCode(), ResultEnum.NEED_LOGIN.getMsg());
        }
        Result result = orderService.queryOrderPayStatus(orderNo, currentUser.getId());
        if (result.isSuccess()) {
            return Result.createBySuccess(true);
        }
        return Result.createBySuccess(false);
    }

    @PostMapping("/alipay_callback.do")
    public Object alipayCallback(HttpServletRequest request) {
        Map<String, String[]> requestParameters = request.getParameterMap();
        // 拼装所需 Map<String, String> 类型参数
        Map<String, String> params = new HashMap<>();
//        requestParameters.forEach((key, value[]) -> {
//            String valueStr = "";
//            for (String valueItem : value) {
//                valueStr += "".equals(valueStr) ? "" : "," + valueItem;
//            }
//            params.put(key, valueStr);
//        });
        for (String name : requestParameters.keySet()) {
            String[] values = requestParameters.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        log.info("[支付宝回调] sign: {}, trade_status: {}, 参数: {}", requestParameters.get("sign"), requestParameters.get("trade_status"), params.toString());

        // 验证支付宝异步回调
        // 除去sign、sign_type两个参数，其中sign参数在rsaCheckV2()方法的getSignCheckContentV2()方法中有移除
        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
            if (!alipayRSACheckedV2) {
                return Result.createByErrorMessage("非法请求，如继续恶意请求你将会负相关法律责任！！！");
            }
        } catch (AlipayApiException e) {
            log.error("支付宝验证回调异常", e);
        }

        // TODO 验证各种数据

        Result result = orderService.aliCallback(params);
        if (result.isSuccess()) {
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallback.RESPONSE_FAILED;
    }
}
