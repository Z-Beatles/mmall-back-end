package cn.waynechu.mmall.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author waynechu
 * Created 2018-05-22 14:21
 */
@Data
public class UserInfoVO {

    private Integer id;

    private String username;

    private String email;

    private String phone;

    private String question;

    private Integer role;

    private Date createTime;

    private Date updateTime;
}
