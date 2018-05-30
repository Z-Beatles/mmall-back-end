package cn.waynechu.mmall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author waynechu
 * Created 2018-05-29 10:45
 */
@Data
public class OrderVO {

    private Long orderNo;

    private BigDecimal payment;

    private Integer paymentType;

    private String paymentTypeDesc;

    private Integer postage;

    private Integer status;

    private String statusDesc;

    private String paymentTime;

    private String sendTime;

    private String endTime;

    private String closeTime;

    private List<OrderItemVO> orderItemVOList;

    private String imageHost;

    private Long shippingId;

    private String receiverName;

    private ShippingVO shippingVO;
}
