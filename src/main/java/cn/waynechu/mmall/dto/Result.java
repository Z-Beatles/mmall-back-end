package cn.waynechu.mmall.dto;

import lombok.Data;

/**
 * @author waynechu
 * Created 2018-05-12 00:22
 */
@Data
public class Result<T> {

    private Integer code;

    private String msg;

    private T data;
}
