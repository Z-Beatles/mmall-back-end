package cn.waynechu.mmall.service.impl;

import cn.waynechu.mmall.common.Const;
import cn.waynechu.mmall.common.Result;
import cn.waynechu.mmall.entity.User;
import cn.waynechu.mmall.mapper.UserMapper;
import cn.waynechu.mmall.service.UserService;
import cn.waynechu.mmall.util.DateTimeUtil;
import cn.waynechu.mmall.util.MD5Util;
import cn.waynechu.mmall.vo.UserInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author waynechu
 * Created 2018-05-21 19:48
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result<UserInfoVO> login(String username, String password) {
        int count = userMapper.checkUsername(username);
        if (count == 0) {
            return Result.createByErrorMessage("用户名不存在");
        }

        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return Result.createByErrorMessage("密码错误");
        }
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfoVO);
        userInfoVO.setCreateTime(DateTimeUtil.toStringFromLocalDateTime(user.getCreateTime()));
        userInfoVO.setUpdateTime(DateTimeUtil.toStringFromLocalDateTime(user.getUpdateTime()));
        return Result.createBySuccess("登录成功", userInfoVO);
    }

    @Override
    public Result<String> register(String username, String password, String email, String phone, String question, String answer) {
        // 校验用户名
        int validateCount = userMapper.checkUsername(username);
        if (validateCount > 0) {
            return Result.createByErrorMessage("用户名已存在");
        }
        // 校验邮箱
        if (email != null) {
            validateCount = userMapper.checkEmail(email);
            if (validateCount > 0) {
                return Result.createByErrorMessage("邮箱已存在");
            }
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
            return Result.createByErrorMessage("注册失败");
        }
        return Result.createBySuccessMessage("注册成功");
    }

    @Override
    public Result<String> checkValid(String value, String type) {
        if (Const.USERNAME.equals(type)) {
            // 校验用户名是否存在
            int resultCount = userMapper.checkUsername(value);
            if (resultCount > 0) {
                return Result.createByErrorMessage("用户名已存在");
            }
        } else if (Const.EMAIL.equals(type)) {
            // 校验邮箱是否存在
            int resultCount = userMapper.checkEmail(value);
            if (resultCount > 0) {
                return Result.createByErrorMessage("邮箱已存在");
            }
        } else {
            return Result.createBySuccessMessage("校验类型不存在");
        }
        return Result.createBySuccessMessage("校验成功");
    }

    @Override
    public Result<String> selectQuestion(String username) {
        Result<String> response = checkValid(username, Const.USERNAME);
        if (response.isSuccess()) {
            return Result.createByErrorMessage("用户不存在");
        }
        String question = userMapper.getQuestionByUsername(username);
        if (question != null) {
            return Result.createBySuccess(question);
        }
        return Result.createByErrorMessage("未设置密码问题");
    }

    @Override
    public Result<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            String forgetToken = UUID.randomUUID().toString();
            // 保存重置密码的token到redis中，有效时常为8小时
            stringRedisTemplate.opsForValue().set(Const.RESET_PASSWORD_TOKEN_PREFIX + username,
                    forgetToken, 8, TimeUnit.HOURS);
            return Result.createBySuccess(forgetToken);
        }
        return Result.createByErrorMessage("问题的答案错误");
    }

    @Override
    public Result<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (forgetToken == null) {
            return Result.createByErrorMessage("参数错误,token需要传递");
        }
        Result validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            // 用户不存在
            return Result.createByErrorMessage("用户不存在");
        }

        // 从缓存中查找token
        String token = stringRedisTemplate.opsForValue().get(Const.RESET_PASSWORD_TOKEN_PREFIX + username);
        if (token == null) {
            return Result.createByErrorMessage("token无效或者已过期");
        }

        if (forgetToken.equals(token)) {
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int resultCount = userMapper.updatePasswordByUsername(username, md5Password);

            if (resultCount > 0) {
                // 更新密码成功删除缓存的token
                stringRedisTemplate.delete(Const.RESET_PASSWORD_TOKEN_PREFIX + username);
                return Result.createBySuccessMessage("修改密码成功");
            }
        } else {
            return Result.createByErrorMessage("token错误,请重新获取重置密码的token");
        }
        return Result.createByErrorMessage("修改密码失败");
    }

    @Override
    public Result<String> resetPassword(String passwordOld, String passwordNew, UserInfoVO userInfoVO) {
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), userInfoVO.getId());
        if (resultCount == 0) {
            return Result.createByErrorMessage("旧密码错误");
        }

        int updateCount = userMapper.updatePasswordByUsername(userInfoVO.getUsername(), MD5Util.MD5EncodeUtf8(passwordNew));
        if (updateCount > 0) {
            return Result.createBySuccessMessage("密码更新成功");
        }
        return Result.createByErrorMessage("密码更新失败");
    }

    @Override
    public Result<UserInfoVO> updateInformation(UserInfoVO currentUser, String email, String phone, String question, String answer) {
        if (email == null && phone == null && question == null && answer == null) {
            return Result.createByErrorMessage("未填写更新内容");
        }

        int resultCount = userMapper.checkEmailByUserId(email, currentUser.getId());
        if (resultCount > 0) {
            return Result.createByErrorMessage("email已存在,请更换email再尝试更新");
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
            return Result.createBySuccess("更新个人信息成功", currentUser);
        }
        return Result.createByErrorMessage("更新个人信息失败");
    }

    @Override
    public Result<UserInfoVO> getInformation(Long userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return Result.createByErrorMessage("找不到当前用户");
        }
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfoVO);
        userInfoVO.setCreateTime(DateTimeUtil.toStringFromLocalDateTime(user.getCreateTime()));
        userInfoVO.setUpdateTime(DateTimeUtil.toStringFromLocalDateTime(user.getUpdateTime()));
        return Result.createBySuccess(userInfoVO);
    }

    @Override
    public Result checkAdminRole(UserInfoVO userInfoVO) {
        if (userInfoVO != null && userInfoVO.getRole() == Const.Role.ROLE_ADMIN) {
            return Result.createBySuccess();
        }
        return Result.createByError();
    }

}
