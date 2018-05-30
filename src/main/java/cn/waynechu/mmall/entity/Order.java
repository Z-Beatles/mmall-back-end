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
public class Order {
    /** 订单id **/
    private Long id;
    /** 订单号 **/
    private Long orderNo;
    /** 用户id **/
    private Long userId;
    /** 收获地址id **/
    private Long shippingId;
    /** 实际付款金额：单位/元，保留两位小数 **/
    private BigDecimal payment;
    /** 支付类型：1在线支付 **/
    private Integer paymentType;
    /** 运费：单位/元 **/
    private Integer postage;
    /** 订单状态：0已取消，10未付款，20已付款，40已发货，50交易成功，60交易关闭 **/
    private Integer status;
    /** 支付时间 **/
    private LocalDateTime paymentTime;
    /** 发货时间 **/
    private LocalDateTime sendTime;
    /** 交易完成时间 **/
    private LocalDateTime endTime;
    /** 交易关闭时间 **/
    private LocalDateTime closeTime;
    /** 创建时间 **/
    private LocalDateTime createTime;
    /** 更新时间 **/
    private LocalDateTime updateTime;
}