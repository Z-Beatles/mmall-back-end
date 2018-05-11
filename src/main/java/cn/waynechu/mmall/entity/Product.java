package cn.waynechu.mmall.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author waynechu
 * Created 2018-05-12 00:17
 */
@Data
public class Product {

    private Long id;

    private Long categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private String subImage;

    private String detail;

    private BigDecimal price;

    private Integer stock;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}
