package cn.waynechu.mmall.service;

import cn.waynechu.mmall.common.Result;
import cn.waynechu.mmall.entity.User;
import cn.waynechu.mmall.vo.UserInfoVO;

/**
 * @author waynechu
 * Created 2018-05-21 19:48
 */
public interface UserService {

    Result<User> login(String username, String password);

    Result<String> register(String username, String password, String email, String phone, String question, String answer);

    Result<String> getQuestionByUsername(String username);

    Result<String> checkAnswer(String username, String question, String answer);

    Result<String> forgetResetPassword(String username, String passwordNew, String forgetToken);

    Result<String> updatePassword(String passwordOld, String passwordNew, User user);

    Result<User> updateUserInfo(User user, String email, String phone, String question, String answer);

    User getCurrentUserInfo(Long userId);

    Result checkAdminRole(User userInfoVO);

    UserInfoVO assembleUserInfoVO(User user);

    Result<String> checkRegisterParam(String param, String type);
}
