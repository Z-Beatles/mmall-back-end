package cn.waynechu.mmall.service.impl;

import cn.waynechu.mmall.cache.TokenCache;
import cn.waynechu.mmall.common.Const;
import cn.waynechu.mmall.common.ServerResponse;
import cn.waynechu.mmall.entity.User;
import cn.waynechu.mmall.mapper.UserMapper;
import cn.waynechu.mmall.service.UserService;
import cn.waynechu.mmall.util.DateTimeUtil;
import cn.waynechu.mmall.util.MD5Util;
import cn.waynechu.mmall.vo.UserInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author waynechu
 * Created 2018-05-21 19:48
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<UserInfoVO> login(String username, String password) {
        int count = userMapper.checkUsername(username);
        if (count == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfoVO);
        userInfoVO.setCreateTime(DateTimeUtil.toStringFromLocalDateTime(user.getCreateTime()));
        userInfoVO.setUpdateTime(DateTimeUtil.toStringFromLocalDateTime(user.getUpdateTime()));
        return ServerResponse.createBySuccess("登录成功", userInfoVO);
    }

    @Override
    public ServerResponse<String> register(String username, String password, String email, String phone, String question, String answer) {
        int validateCount = userMapper.checkUsername(username);
        if (validateCount > 0) {
            return ServerResponse.createByErrorMessage("用户名已存在");
        }

        validateCount = userMapper.checkEmail(email);
        if (validateCount > 0) {
            return ServerResponse.createByErrorMessage("邮箱已存在");
        }
        User user = new User();
        user.setUsername(username);
        // MD5算法生成密码摘要
        user.setPassword(MD5Util.MD5EncodeUtf8(password));
        user.setEmail(email);
        user.setPhone(phone);
        user.setQuestion(question);
        user.setAnswer(answer);
        // 默认设置为普通用户
        user.setRole(Const.Role.ROLE_CUSTOMER);

        int resultCount = userMapper.insertSelective(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<String> checkValid(String value, String type) {
        if (Const.USERNAME.equals(type)) {
            // 校验用户名是否存在
            int resultCount = userMapper.checkUsername(value);
            if (resultCount > 0) {
                return ServerResponse.createByErrorMessage("用户名已存在");
            }
        } else if (Const.EMAIL.equals(type)) {
            // 校验邮箱是否存在
            int resultCount = userMapper.checkEmail(value);
            if (resultCount > 0) {
                return ServerResponse.createByErrorMessage("邮箱已存在");
            }
        } else {
            return ServerResponse.createBySuccessMessage("校验类型不存在");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    @Override
    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse<String> response = checkValid(username, Const.USERNAME);
        if (response.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.getQuestionByUsername(username);
        if (question != null) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("未设置密码问题");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误");
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (forgetToken == null) {
            return ServerResponse.createByErrorMessage("参数错误,token需要传递");
        }
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            // 用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (token == null) {
            return ServerResponse.createByErrorMessage("token无效或者已过期");
        }

        if (forgetToken.equals(token)) {
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int resultCount = userMapper.updatePasswordByUsername(username, md5Password);

            if (resultCount > 0) {
                // 删除缓存的token
                TokenCache.deleteKey(TokenCache.TOKEN_PREFIX + username);
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        } else {
            return ServerResponse.createByErrorMessage("token错误,请重新获取重置密码的token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, UserInfoVO userInfoVO) {
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), userInfoVO.getId());
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("旧密码错误");
        }

        int updateCount = userMapper.updatePasswordByUsername(userInfoVO.getUsername(), MD5Util.MD5EncodeUtf8(passwordNew));
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }

    @Override
    public ServerResponse<UserInfoVO> updateInformation(UserInfoVO currentUser, String email, String phone, String question, String answer) {
        if (email == null && phone == null && question == null && answer == null) {
            return ServerResponse.createByErrorMessage("未填写更新内容");
        }

        int resultCount = userMapper.checkEmailByUserId(email, currentUser.getId());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("email已存在,请更换email再尝试更新");
        }

        User updateUser = new User();
        updateUser.setId(currentUser.getId());
        updateUser.setEmail(email);
        updateUser.setPhone(phone);
        updateUser.setQuestion(question);
        updateUser.setAnswer(answer);
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) {
            if (email != null) {
                currentUser.setEmail(email);
            }
            if (phone != null) {
                currentUser.setPhone(phone);
            }
            if (question != null) {
                currentUser.setQuestion(question);
            }
            return ServerResponse.createBySuccess("更新个人信息成功", currentUser);
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }

    @Override
    public ServerResponse<UserInfoVO> getInformation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfoVO);
        userInfoVO.setCreateTime(DateTimeUtil.toStringFromLocalDateTime(user.getCreateTime()));
        userInfoVO.setUpdateTime(DateTimeUtil.toStringFromLocalDateTime(user.getUpdateTime()));
        return ServerResponse.createBySuccess(userInfoVO);
    }

    @Override
    public ServerResponse checkAdminRole(UserInfoVO userInfoVO) {
        if (userInfoVO != null && userInfoVO.getRole() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

}
