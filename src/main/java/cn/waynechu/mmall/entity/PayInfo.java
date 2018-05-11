package cn.waynechu.mmall.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author waynechu
 * Created 2018-05-12 00:17
 */
@Data
public class PayInfo {

    private Long id;

    private Long userId;

    private Long orderNo;

    private Byte payPlatform;

    private String platformNumber;

    private String platformStatus;

    private Date createTime;

    private Date updateTime;
}
