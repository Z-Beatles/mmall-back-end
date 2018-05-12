package cn.waynechu.mmall.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author waynechu
 * Created 2018-05-12 19:59
 */
@Data
public class UserDTO {

    private Long id;

    private String username;

    private String email;

    private String phone;
}
