package cn.waynechu.mmall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author waynechu
 * Created 2018-05-21 11:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    /** 商品id **/
    private Long id;
    /** 分类id **/
    private Long categoryId;
    /** 商品名称 **/
    private String name;
    /** 商品副标题 **/
    private String subtitle;
    /** 商品主图：url相对地址 **/
    private String mainImage;
    /** 图片地址：json格式，扩展用 **/
    private String subImages;
    /** 商品详情介绍 **/
    private String detail;
    /** 商品价格：单位/元，保留两位小数 **/
    private BigDecimal price;
    /** 库存数量 **/
    private Integer stock;
    /** 商品状态：0下架，1在售，2删除，默认0 **/
    private Integer status;
    /** 创建时间 **/
    private LocalDateTime createTime;
    /** 更新时间 **/
    private LocalDateTime updateTime;
}