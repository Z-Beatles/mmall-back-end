package cn.waynechu.mmall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author waynechu
 * Created 2018-05-29 10:48
 */
@Data
public class OrderItemVO {

    private Long orderNo;

    private Integer productId;

    private String productName;

    private String productImage;

    private BigDecimal currentUnitPrice;

    private Integer quantity;

    private BigDecimal totalPrice;

    private String createTime;
}
