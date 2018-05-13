package cn.waynechu.mmall.entity;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author waynechu
 * Created 2018-05-12 00:19
 */
@Data
public class User implements Serializable {

    private Long id;

    private String username;

    private String passwordHash;

    private String passwordSalt;

    private String passwordAlgo;

    private Integer passwordIteration;

    private String email;

    private String phone;

    private String question;

    private String answer;

    private Date createTime;

    private Date updateTime;
}
