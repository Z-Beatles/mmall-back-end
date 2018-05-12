package cn.waynechu.mmall.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author waynechu
 * Created 2018-05-12 00:13
 */
@Data
public class Cart implements Serializable {

    private Long id;

    private Long userId;

    private Long productId;

    private Integer quantity;

    private Integer checked;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;
}
