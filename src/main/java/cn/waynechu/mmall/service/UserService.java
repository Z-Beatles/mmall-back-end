package cn.waynechu.mmall.service;


import cn.waynechu.mmall.dto.UserDTO;
import cn.waynechu.mmall.entity.User;

/**
 * @author waynechu
 * Created 2018-05-12 13:18
 */
public interface UserService {

    /**
     * 根据帐号获取用户信息
     *
     * @param account 帐号(用户名、邮箱、移动电话)
     * @return 用户信息
     */
    User getByAccount(String account);

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

    /**
     * 检查用户名是否有效
     *
     * @param username 用户名
     * @return 有效:true
     */
    boolean checkUsername(String username);

    /**
     * 检查邮箱是否有效
     *
     * @param email 邮箱
     * @return 有效:true
     */
    boolean checkEmail(String email);

    /**
     * 检查手机号是否有效
     *
     * @param mobile 手机号
     * @return 有效:true
     */
    boolean checkMobile(String mobile);
}