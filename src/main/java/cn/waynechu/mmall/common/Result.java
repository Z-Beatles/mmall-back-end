package cn.waynechu.mmall.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author waynechu
 * Created 2018-05-21 19:44
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> implements Serializable {

    private int code;
    private String msg;
    private T data;

    private Result(int code) {
        this.code = code;
    }

    private Result(int code, T data) {
        this.code = code;
        this.data = data;
    }

    private Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.code == ResultEnum.SUCCESS.getCode();
    }

    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }


    public static <T> Result<T> createBySuccess() {
        return new Result<>(ResultEnum.SUCCESS.getCode());
    }

    public static <T> Result<T> createBySuccessMessage(String msg) {
        return new Result<>(ResultEnum.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> createBySuccess(T data) {
        return new Result<>(ResultEnum.SUCCESS.getCode(), data);
    }

    public static <T> Result<T> createBySuccess(String msg, T data) {
        return new Result<>(ResultEnum.SUCCESS.getCode(), msg, data);
    }


    public static <T> Result<T> createByError() {
        return new Result<>(ResultEnum.ERROR.getCode(), ResultEnum.ERROR.getMsg());
    }


    public static <T> Result<T> createByErrorMessage(String errorMessage) {
        return new Result<>(ResultEnum.ERROR.getCode(), errorMessage);
    }

    public static <T> Result<T> createByErrorCodeMessage(int errorCode, String errorMessage) {
        return new Result<>(errorCode, errorMessage);
    }

    public static <T> Result<T> createByError(int errorCode, String errorMessage, T data) {
        return new Result<>(errorCode, errorMessage, data);
    }
}
