package cn.waynechu.mmall.entity;


import lombok.Data;

import java.util.Date;

/**
 * @author waynechu
 * Created 2018-05-12 00:19
 */
@Data
public class User {
    private Long id;

    private String username;

    private String password;

    private String email;

    private String phone;

    private String question;

    private String answer;

    private Integer role;

    private Date createTime;

    private Date updateTime;
}
