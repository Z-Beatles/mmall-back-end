package cn.waynechu.mmall.service;


import cn.waynechu.mmall.dto.UserDTO;
import cn.waynechu.mmall.entity.User;

/**
 * @author waynechu
 * Created 2018-05-12 13:18
 */
public interface UserService {

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getByUsername(String username);

    /**
     * 用户注册
     *
     * @param username 用户名
     * @param password 用户密码
     * @return 用户id
     */
    UserDTO insertUser(String username, String password);

    /**
     * 获取当前在线的用户id
     *
     * @return 当前在线的用户id
     */
    Long getCurrentUserId();

    /**
     * 根据用户id获取用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    UserDTO getUserByUserId(Long userId);
}