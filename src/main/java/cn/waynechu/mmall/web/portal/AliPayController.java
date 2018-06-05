package cn.waynechu.mmall.web.portal;

import cn.waynechu.mmall.common.Const;
import cn.waynechu.mmall.common.Result;
import cn.waynechu.mmall.service.AlipayService;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author waynechu
 * Created 2018-06-05 12:19
 */
@Slf4j
@Api(tags = "门户-支付宝支付接口")
@RestController
@RequestMapping("/v1/pay")
public class AliPayController {

    @Autowired
    private AlipayService alipayService;

    @ApiOperation(value = "获取支付宝支付状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderNo", value = "订单号", paramType = "query", required = true)
    })
    @GetMapping("/query_pay_status.do")
    public Result<Boolean> queryPayStatus(Long orderNo) {
        Result result = alipayService.queryPayStatus(orderNo);
        if (result.isSuccess()) {
            return Result.createBySuccess(true);
        }
        return Result.createBySuccess(false);
    }

    @ApiOperation(value = "支付宝回调接口", notes = "[Tip] 该接口只供支付宝回调使用")
    @PostMapping(value = "/alipay_callback.do")
    public String alipayCallback(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            params.put(parameterName, request.getParameter(parameterName));
        }
        log.info("支付宝回调,sign:{},trade_status:{},参数:{}", params.get("sign"), params.get("trade_status"), params.toString());

        // 验签
        // 除去sign、sign_type两个参数，其中sign参数在rsaCheckV2()方法的getSignCheckContentV2()方法中有移除
        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
            if (!alipayRSACheckedV2) {
                return Const.AlipayCallback.RESPONSE_FAILED;
            }
        } catch (AlipayApiException e) {
            log.error("支付宝验证回调异常", e);
            return Const.AlipayCallback.RESPONSE_FAILED;
        }

        // 校验参数并生成支付信息
        Result result = alipayService.checkPayParams(params);
        if (result.isSuccess()) {
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallback.RESPONSE_FAILED;
    }
}
