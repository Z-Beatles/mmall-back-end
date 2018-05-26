package cn.waynechu.mmall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author waynechu
 * Created 2018-05-26 11:10
 */
@Data
public class CartVO {

    private List<CartProductVO> cartProductVOList;

    /**
     * 购物车总价
     **/
    private BigDecimal cartTotalPrice;

    /**
     * 购物车中商品是否全部勾选
     **/
    private Boolean allChecked;

    private String imageHost;
}
