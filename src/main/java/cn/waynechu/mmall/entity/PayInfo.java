package cn.waynechu.mmall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author waynechu
 * Created 2018-05-21 11:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayInfo {
    /** 支付信息id **/
    private Long id;
    /** 用户id **/
    private Long userId;
    /** 订单号 **/
    private Long orderNo;
    /** 支付平台：1支付宝，2微信 **/
    private Integer payPlatform;
    /** 平台支付流水号 **/
    private String platformNumber;
    /** 平台支付状态 **/
    private String platformStatus;
    /** 创建时间 **/
    private LocalDateTime createTime;
    /** 更新时间 **/
    private LocalDateTime updateTime;
}