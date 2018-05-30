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
public class User {
    /** 用户id **/
    private Long id;
    /** 用户名 **/
    private String username;
    /** 用户密码：MD5摘要 **/
    private String password;
    /** 邮箱 **/
    private String email;
    /** 电话 **/
    private String phone;
    /** 找回密码问题 **/
    private String question;
    /** 找回密码答案 **/
    private String answer;
    /** 角色：0普通用户，1管理员，默认0 **/
    private Integer role;
    /** 创建时间 **/
    private LocalDateTime createTime;
    /** 更新时间 **/
    private LocalDateTime updateTime;
}