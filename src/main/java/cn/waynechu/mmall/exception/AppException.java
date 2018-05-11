package cn.waynechu.mmall.exception;

import cn.waynechu.mmall.emuns.ResultEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author waynechu
 * Created 2018-05-12 00:26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AppException extends RuntimeException {

    private Integer errorCode;

    private String errorMessage;

    private Throwable errorCause;

    public AppException(ResultEnum resultEnum) {
        this.errorCode = resultEnum.getCode();
        this.errorMessage = resultEnum.getMsg();
    }

    public AppException(Integer errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public AppException(Integer errorCode, String errorMessage, Throwable errorCause) {
        super(errorMessage, errorCause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorCause = errorCause;
    }
}
