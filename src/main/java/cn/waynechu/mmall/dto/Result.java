package cn.waynechu.mmall.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author waynechu
 * Created 2018-05-12 00:22
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {

    private Integer code;

    private String msg;

    private T data;
}
