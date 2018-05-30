package cn.waynechu.mmall.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author waynechu
 * Created 2018-05-22 14:21
 */
@Data
public class UserInfoVO {

    private Long id;

    private String username;

    private String email;

    private String phone;

    private String question;

    private Integer role;

    private String createTime;

    private String updateTime;
}
