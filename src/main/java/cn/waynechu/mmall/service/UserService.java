package cn.waynechu.mmall.service;

import cn.waynechu.mmall.common.Result;
import cn.waynechu.mmall.vo.UserInfoVO;

/**
 * @author waynechu
 * Created 2018-05-21 19:48
 */
public interface UserService {

    Result<UserInfoVO> login(String username, String password);

    Result<String> register(String username, String password, String email, String phone, String question, String answer);

    Result<String> checkValid(String param, String type);

    Result<String> selectQuestion(String username);

    Result<String> checkAnswer(String username, String question, String answer);

    Result<String> forgetResetPassword(String username, String passwordNew, String forgetToken);

    Result<String> resetPassword(String passwordOld, String passwordNew, UserInfoVO userInfoVO);

    Result<UserInfoVO> updateInformation(UserInfoVO currentUser, String email, String phone, String question, String answer);

    Result<UserInfoVO> getInformation(Long userId);

    Result checkAdminRole(UserInfoVO userInfoVO);
}
