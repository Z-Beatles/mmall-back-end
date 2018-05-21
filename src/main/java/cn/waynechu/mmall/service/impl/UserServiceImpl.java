package cn.waynechu.mmall.service.impl;

import cn.waynechu.mmall.common.Const;
import cn.waynechu.mmall.common.ServerResponse;
import cn.waynechu.mmall.entity.User;
import cn.waynechu.mmall.mapper.UserMapper;
import cn.waynechu.mmall.service.UserService;
import cn.waynechu.mmall.util.MD5Util;
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
    public ServerResponse<User> login(String username, String password) {
        int count = userMapper.checkUsername(username);
        if (count == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(null);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        int validateCount = userMapper.checkUsername(user.getUsername());
        if (validateCount > 0) {
            return ServerResponse.createByErrorMessage("用户名已存在");
        }

        validateCount = userMapper.checkEmail(user.getEmail());
        if (validateCount > 0) {
            return ServerResponse.createByErrorMessage("邮箱已存在");
        }
        // 默认设置为普通用户
        user.setRole(Const.Role.ROLE_CUSTOMER);
        // MD5算法生成密码摘要
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<String> checkValid(String param, String type) {
        // 校验用户名是否存在
        if (Const.USERNAME.equals(type)) {
            int resultCount = userMapper.checkUsername(param);
            if (resultCount > 0) {
                return ServerResponse.createByErrorMessage("用户名已存在");
            }
        }
        // 校验邮箱是否存在
        if (Const.EMAIL.equals(type)) {
            int resultCount = userMapper.checkEmail(param);
            if (resultCount > 0) {
                return ServerResponse.createByErrorMessage("邮箱已存在");
            }
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
            return ServerResponse.createBySuccess();
        }
        return null;
    }
}
