package cn.waynechu.mmall.handler;

import cn.waynechu.mmall.dto.Result;
import cn.waynechu.mmall.emuns.ResultEnum;
import cn.waynechu.mmall.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author waynechu
 * Created 2018-04-14 13:25
 */
@ControllerAdvice
@Slf4j
public class AppExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handle(Exception e) {
        if (e instanceof AppException) {
            AppException appException = (AppException) e;
            return Result.createByError(appException.getErrorCode(), appException.getErrorMessage());
        } else if (e instanceof MissingServletRequestParameterException) {
            // 缺少请求参数
            String parameterName = ((MissingServletRequestParameterException) e).getParameterName();
            return Result.createByError(ResultEnum.MISSING_REQUEST_PARAMETER_ERROR, parameterName);
        } else {
            log.error("[系统异常]", e);
            return Result.createByError(ResultEnum.ERROR.getCode(), e.getMessage());
        }
    }
}
