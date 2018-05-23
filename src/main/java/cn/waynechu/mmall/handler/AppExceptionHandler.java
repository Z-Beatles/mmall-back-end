package cn.waynechu.mmall.handler;

import cn.waynechu.mmall.common.ResponseCode;
import cn.waynechu.mmall.common.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author waynechu
 * Created 2018-05-22 16:49
 */
@Slf4j
@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ServerResponse handle(Exception e) {
        if (e instanceof MissingServletRequestParameterException) {
            // 缺少请求参数
            String parameterName = ((MissingServletRequestParameterException) e).getParameterName();
            int code = ResponseCode.MISSING_REQUEST_PARAMETER.getCode();
            String desc = ResponseCode.MISSING_REQUEST_PARAMETER.getDesc();
            return ServerResponse.createByError(code, desc, parameterName);
        } else {
            log.error("[系统异常]", e);
            return ServerResponse.createBySuccessMessage("[系统异常] " + e.toString());
        }
    }
}
