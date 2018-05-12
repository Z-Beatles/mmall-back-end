package cn.waynechu.mmall.entity;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author waynechu
 * Created 2018-05-12 13:52
 */
@Data
public class UserRole implements Serializable {

    private Long id;

    private Long userId;

    private Long roleId;

    private Date createTime;

    private Date updateTime;
}
