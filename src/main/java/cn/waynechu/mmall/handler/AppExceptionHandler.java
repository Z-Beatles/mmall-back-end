package cn.waynechu.mmall.handler;

import cn.waynechu.mmall.common.Result;
import cn.waynechu.mmall.common.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author waynechu
 * Created 2018-05-22 16:49
 */
@Slf4j
@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public Result missingServletRequestParameterException(MissingServletRequestParameterException e) {
        // 缺少请求参数
        String parameterName = e.getParameterName();
        int code = ResultEnum.MISSING_REQUEST_PARAMETER.getCode();
        String desc = ResultEnum.MISSING_REQUEST_PARAMETER.getMsg();
        return Result.createByError(code, desc, parameterName);
    }

    @ExceptionHandler(value = Exception.class)
    public Result unknownException(Exception e) {
        log.error("[系统异常]", e);
        return Result.createBySuccessMessage("[系统异常] " + e.toString());
    }
}
