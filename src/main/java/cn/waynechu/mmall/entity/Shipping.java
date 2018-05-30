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
public class Shipping {
    /** 收获地址id **/
    private Long id;
    /** 用户id **/
    private Long userId;
    /** 收货人姓名 **/
    private String receiverName;
    /** 收货人固定电话 **/
    private String receiverPhone;
    /** 收货人手机号码 **/
    private String receiverMobile;
    /** 省份 **/
    private String receiverProvince;
    /** 城市 **/
    private String receiverCity;
    /** 区/县 **/
    private String receiverDistrict;
    /** 详细地址 **/
    private String receiverAddress;
    /** 邮编 **/
    private String receiverZip;
    /** 创建时间 **/
    private LocalDateTime createTime;
    /** 更新时间 **/
    private LocalDateTime updateTime;
}