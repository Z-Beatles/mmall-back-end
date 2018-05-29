package cn.waynechu.mmall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author waynechu
 * Created 2018-05-23 15:23
 */
@Data
public class ProductDetialVO {

    private Integer id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private String subImages;

    private String detail;

    private BigDecimal price;

    private Integer stock;

    private Integer status;

    private String createTime;

    private String updateTime;


    private String imageHost;

    private Integer parentCategoryId;
}
