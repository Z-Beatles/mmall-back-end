package cn.waynechu.mmall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author waynechu
 * Created 2018-05-26 11:54
 */
@Data
public class CartProductVO {

    private Long id;

    private Long userId;

    private Long productId;

    /**
     * 购物车中此商品的数量
     **/
    private Integer quantity;

    private String productName;

    private String productSubtitle;

    private String productMainImage;

    private BigDecimal productPrice;

    private Integer productStatus;

    private BigDecimal productTotalPrice;

    private Integer productStock;

    /**
     * 商品是否勾选
     **/
    private Integer productChecked;

    /**
     * 限制数量的一个返回结果
     **/
    private String limitQuantity;

}
