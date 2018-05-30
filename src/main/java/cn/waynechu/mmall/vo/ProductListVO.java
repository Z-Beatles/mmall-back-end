package cn.waynechu.mmall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author waynechu
 * Created 2018-05-23 19:35
 */
@Data
public class ProductListVO {

    private Long id;

    private Long categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private BigDecimal price;

    private Integer status;

    private String imageHost;

}
