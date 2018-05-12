package cn.waynechu.mmall.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author waynechu
 * Created 2018-05-12 00:16
 */
@Data
public class OrderItem implements Serializable {

    private Long id;

    private Long userId;

    private Long orderNo;

    private Long productId;

    private String productName;

    private String productImage;

    private BigDecimal currentUnitPrice;

    private Integer quantity;

    private BigDecimal totalPrice;

    private Date createTime;

    private Date updateTime;
}
