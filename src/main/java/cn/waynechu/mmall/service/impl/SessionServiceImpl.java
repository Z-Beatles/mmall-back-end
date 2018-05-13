package cn.waynechu.mmall.service.impl;

import cn.waynechu.mmall.dto.UserDTO;
import cn.waynechu.mmall.emuns.ResultEnum;
import cn.waynechu.mmall.entity.User;
import cn.waynechu.mmall.exception.AppException;
import cn.waynechu.mmall.service.SessionService;
import cn.waynechu.mmall.shiro.LoginAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author waynechu
 * Created 2018-05-12 17:59
 */
@Service
@Slf4j
public class SessionServiceImpl implements SessionService {

    @Override
    public UserDTO doLogin(String loginType, String account, String password, boolean rememberMe, String host) {
        Subject currentUser = SecurityUtils.getSubject();
        if (!currentUser.isAuthenticated()) {
            LoginAuthenticationToken token = new LoginAuthenticationToken(loginType, account, password, rememberMe, host);
            token.setRememberMe(rememberMe);
            try {
                currentUser.login(token);
                User principal = (User) currentUser.getPrincipal();
                UserDTO userDTO = new UserDTO();
                BeanUtils.copyProperties(principal, userDTO);
                log.info("账号 {} 登陆系统", account);
                return userDTO;
            } catch (UnknownAccountException e) {
                log.warn("账号 {} 不存在", account);
                throw new AppException(ResultEnum.ACCOUNT_NOT_EXIST_ERROR);
            } catch (IncorrectCredentialsException e) {
                log.warn("密码错误，账号：{}", account);
                throw new AppException(ResultEnum.WRONG_PASSWORD_ERROR);
            } catch (AuthenticationException e) {
                log.warn("登录失败", e);
                throw new AppException(ResultEnum.LOGIN_FAILED_ERROR);
            } catch (Exception e) {
                log.error("系统异常", e);
                throw new AppException(ResultEnum.SYSTEM_ERROR);
            }
        }
        throw new AppException(ResultEnum.REPEAT_LOGIN_ERROR);
    }

    @Override
    public Long doLogout() {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated()) {
            User principal = (User) currentUser.getPrincipal();
            String account = principal.getUsername();
            try {
                currentUser.logout();
                log.info("账号 {} 退出系统", account);
                return principal.getId();
            } catch (Exception e) {
                log.error("系统异常", e);
            }
        }
        throw new AppException(ResultEnum.NOT_LOGIN_ERROR);
    }
}
