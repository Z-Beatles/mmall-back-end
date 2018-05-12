package cn.waynechu.mmall.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author waynechu
 * Created 2018-05-12 00:15
 */
@Data
public class Order implements Serializable {

    private Long id;

    private Long orderNo;

    private Long userId;

    private Long shippingId;

    private BigDecimal payment;

    private Integer paymentType;

    private BigDecimal postage;

    private Integer status;

    private Date paymentTime;

    private Date sendTime;

    private Date endTime;

    private Date closeTime;

    private Date createTime;

    private Date updateTime;
}
