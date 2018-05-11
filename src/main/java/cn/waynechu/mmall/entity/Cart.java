package cn.waynechu.mmall.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author waynechu
 * Created 2018-05-12 00:13
 */
@Data
public class Cart {

    private Long id;

    private Long userId;

    private Long productId;

    private Integer quantity;

    private Integer checked;

    private Date createTime;

    private Date updateTime;
}
