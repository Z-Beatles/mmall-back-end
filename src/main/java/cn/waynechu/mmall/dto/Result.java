package cn.waynechu.mmall.dto;

import cn.waynechu.mmall.emuns.ResultEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author waynechu
 * Created 2018-05-16 12:21
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> implements Serializable {

    private int code;

    private String msg;

    private T data;

    private Result(int code) {
        this.code = code;
    }

    private Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
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

    @JsonIgnore
    public boolean isSuccess() {
        return this.code == ResultEnum.SUCCESS.getCode();
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public static <T> Result<T> createBySuccess() {
        return new Result<>(ResultEnum.SUCCESS.getCode());
    }

    public static <T> Result<T> createBySuccess(String msg) {
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

    public static <T> Result<T> createByError(ResultEnum resultEnum) {
        return new Result<>(resultEnum.getCode(), resultEnum.getMsg());
    }

    public static <T> Result<T> createByError(ResultEnum resultEnum, T data) {
        return new Result<>(resultEnum.getCode(), resultEnum.getMsg(), data);
    }

    public static <T> Result<T> createByError(int errorCode, String errorMsg) {
        return new Result<>(errorCode, errorMsg);
    }
}
