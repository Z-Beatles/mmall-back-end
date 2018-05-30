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
public class OrderItem {
    /** 订单子详情id **/
    private Long id;
    /** 用户id **/
    private Long userId;
    /** 订单号 **/
    private Long orderNo;
    /** 商品id **/
    private Long productId;
    /** 商品名称 **/
    private String productName;
    /** 商品图片地址 **/
    private String productImage;
    /** 生成订单时的商品单价：单位/元，保留两位小数 **/
    private BigDecimal currentUnitPrice;
    /** 商品数量 **/
    private Integer quantity;
    /** 商品总价：单位/元，保留两位小数 **/
    private BigDecimal totalPrice;
    /** 创建时间 **/
    private LocalDateTime createTime;
    /** 更新时间 **/
    private LocalDateTime updateTime;
}