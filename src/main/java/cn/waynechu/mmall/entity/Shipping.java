package cn.waynechu.mmall.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author waynechu
 * Created 2018-05-12 00:18
 */
@Data
public class Shipping implements Serializable {

    private Long id;

    private Long userId;

    private String receiverName;

    private String receiverPhone;

    private String receiverMobile;

    private String receiverProvince;

    private String receiverCity;

    private String receiverDistrict;

    private String receiverAddress;

    private String receiverZip;

    private Date createTime;

    private Date updateTime;
}
