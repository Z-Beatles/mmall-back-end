package cn.waynechu.mmall.service;

import cn.waynechu.mmall.common.ServerResponse;
import cn.waynechu.mmall.vo.UserInfoVO;

/**
 * @author waynechu
 * Created 2018-05-21 19:48
 */
public interface UserService {

    ServerResponse<UserInfoVO> login(String username, String password);

    ServerResponse<String> register(String username, String password, String email, String phone, String question, String answer);

    ServerResponse<String> checkValid(String value, String type);

    ServerResponse<String> selectQuestion(String username);

    ServerResponse<String> checkAnswer(String username, String question, String answer);

    ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken);

    ServerResponse<String> resetPassword(String passwordOld, String passwordNew, UserInfoVO userInfoVO);

    ServerResponse<UserInfoVO> updateInformation(UserInfoVO currentUser, String email, String phone, String question, String answer);

    ServerResponse<UserInfoVO> getInformation(Integer userId);

    ServerResponse checkAdminRole(UserInfoVO userInfoVO);
}
