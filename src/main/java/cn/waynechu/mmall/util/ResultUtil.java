package cn.waynechu.mmall.util;

import cn.waynechu.mmall.dto.Result;
import cn.waynechu.mmall.emuns.ResultEnum;

/**
 * @author waynechu
 * Created 2018-05-12 00:23
 */
public class ResultUtil {

    private ResultUtil() {
    }

    private static <T> Result<T> info(ResultEnum resultEnum, T t) {
        Result<T> result = new Result<>();
        result.setCode(resultEnum.getCode());
        result.setMsg(resultEnum.getMsg());
        result.setData(t);
        return result;
    }

    public static <T> Result<T> success(ResultEnum resultEnum, T t) {
        return info(resultEnum, t);
    }

    public static <T> Result<T> success(T t) {
        return info(ResultEnum.SUCCESS, t);
    }

    public static <T> Result<T> success() {
        return info(ResultEnum.SUCCESS, null);
    }

    public static <T> Result<T> error(ResultEnum resultEnum) {
        return info(resultEnum, null);
    }

    public static <T> Result<T> error(ResultEnum resultEnum, T t) {
        return info(resultEnum, t);
    }

    public static Result error(Integer errorCode, String errorMessage) {
        Result result = new Result();
        result.setCode(errorCode);
        result.setMsg(errorMessage);
        return result;
    }
}
