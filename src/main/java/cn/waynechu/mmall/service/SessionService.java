package cn.waynechu.mmall.service;

import cn.waynechu.mmall.dto.UserDTO;

/**
 * @author waynechu
 * Created 2018-05-12 17:59
 */
public interface SessionService {

    /**
     * 用户登录
     *
     * @param loginType  登录类型
     * @param account    帐号
     * @param password   密码
     * @param rememberMe 记住我
     * @param host       IP地址
     * @return 用户信息
     */
    UserDTO doLogin(String loginType, String account, String password, boolean rememberMe, String host);

    /**
     * 用户退出
     *
     * @return 用户id
     */
    Long doLogout();
}
