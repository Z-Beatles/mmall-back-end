package cn.waynechu.mmall.service.impl;

import cn.waynechu.mmall.common.Const;
import cn.waynechu.mmall.common.Result;
import cn.waynechu.mmall.entity.User;
import cn.waynechu.mmall.mapper.UserMapper;
import cn.waynechu.mmall.service.UserService;
import cn.waynechu.mmall.util.MD5Util;
import cn.waynechu.mmall.util.RegexUtil;
import cn.waynechu.mmall.vo.UserInfoVO;
import org.apache.commons.lang.StringUtils;
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
    public Result<User> login(String username, String password) {
        // 判断用户是否存在
        int count = userMapper.countByUsername(username);
        if (count == 0) {
            return Result.createByErrorMessage("用户名不存在");
        }

        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectByUsernameAndPassword(username, md5Password);
        if (user == null) {
            return Result.createByErrorMessage("密码错误");
        }
        return Result.createBySuccess("登录成功", user);
    }

    @Override
    public Result<String> register(String username, String password, String email, String phone, String question, String answer) {
        // 校验用户名格式
        if (!RegexUtil.matchUsername(username)) {
            return Result.createByErrorMessage("用户名格式不正确，需为4到16位的字母、数字、下划线或减号");
        }
        // 校验密码格式
        if (!RegexUtil.matchPassword(password)) {
            return Result.createByErrorMessage("密码格式不正确，需以字母开头，长度在6~18之间的非空字符");
        }
        // 校验邮箱格式
        if (StringUtils.isNotBlank(email) && !RegexUtil.matchEmail(email)) {
            return Result.createByErrorMessage("邮箱格式不正确");
        }
        // 校验电话格式
        if (StringUtils.isNotBlank(phone) && !RegexUtil.matchMobile(phone)) {
            return Result.createByErrorMessage("电话号码格式不正确");
        }

        User user = new User();
        // 检查用户名是否已被注册
        int countUsername = userMapper.countByUsername(username);
        if (countUsername > 0) {
            return Result.createByErrorMessage("用户名已存在");
        }
        user.setUsername(username);
        // MD5算法生成密码摘要
        user.setPassword(MD5Util.MD5EncodeUtf8(password));

        // 检查邮箱是否已被注册
        if (StringUtils.isNotBlank(email)) {
            int emailCount = userMapper.countByEmail(email);
            if (emailCount > 0) {
                return Result.createByErrorMessage("该邮箱已被注册");
            }
            user.setEmail(email);
        }
        // 检查电话是否已被注册
        if (StringUtils.isNotBlank(phone)) {
            int phoneCount = userMapper.countByPhone(phone);
            if (phoneCount > 0) {
                return Result.createByErrorMessage("该电话已被注册");
            }
            user.setPhone(phone);
        }

        if (StringUtils.isNotBlank(question)) {
            user.setQuestion(question);
        }
        if (StringUtils.isNotBlank(answer)) {
            user.setAnswer(answer);
        }
        // 默认设置为普通用户
        user.setRole(Const.Role.ROLE_CUSTOMER);

        int resultCount = userMapper.insertSelective(user);
        if (resultCount == 0) {
            return Result.createByErrorMessage("注册失败");
        }
        return Result.createBySuccessMessage("注册成功");
    }

    @Override
    public Result<String> getQuestionByUsername(String username) {
        int countUsername = userMapper.countByUsername(username);
        if (countUsername == 0) {
            return Result.createByErrorMessage("用户不存在");
        }

        String question = userMapper.selectQuestionByUsername(username);
        if (question != null) {
            return Result.createBySuccess(question);
        }
        return Result.createByErrorMessage("未设置密保问题，请申诉找回");
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
        // 校验密码格式
        if (!RegexUtil.matchPassword(passwordNew)) {
            return Result.createByErrorMessage("密码格式不正确，需以字母开头，长度在6~18的非空字符");
        }
        // 检查用户是否存在
        int countUsername = userMapper.countByUsername(username);
        if (countUsername == 0) {
            return Result.createByErrorMessage("用户不存在");
        }

        // 从缓存中查找token
        String token = stringRedisTemplate.opsForValue().get(Const.RESET_PASSWORD_TOKEN_PREFIX + username);
        if (token == null) {
            return Result.createByErrorMessage("token无效或者已过期");
        }

        if (forgetToken.equals(token)) {
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int updateCount = userMapper.updatePasswordByUsername(username, md5Password);

            if (updateCount > 0) {
                // 更新密码成功，删除缓存的token
                stringRedisTemplate.delete(Const.RESET_PASSWORD_TOKEN_PREFIX + username);
                return Result.createBySuccessMessage("修改密码成功");
            }
        } else {
            return Result.createByErrorMessage("token错误，请重新获取重置密码的token");
        }
        return Result.createByErrorMessage("修改密码失败");
    }

    @Override
    public Result<String> resetPassword(String passwordOld, String passwordNew, User user) {
        // 校验密码格式
        if (!RegexUtil.matchPassword(passwordNew)) {
            return Result.createByErrorMessage("密码格式不正确，需以字母开头，长度在6~18的非空字符");
        }

        int resultCount = userMapper.countByUserIdAndPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (resultCount == 0) {
            return Result.createByErrorMessage("旧密码错误");
        }

        int updateCount = userMapper.updatePasswordByUsername(user.getUsername(), MD5Util.MD5EncodeUtf8(passwordNew));
        if (updateCount > 0) {
            return Result.createBySuccessMessage("密码更新成功");
        }
        return Result.createByErrorMessage("密码更新失败");
    }

    @Override
    public Result<User> updateUserInfo(User user, String email, String phone, String question, String answer) {
        if (StringUtils.isBlank(email) && StringUtils.isBlank(phone) && StringUtils.isBlank(question) && StringUtils.isBlank(answer)) {
            return Result.createByErrorMessage("请输入要修改的个人信息");
        }
        // 校验邮箱格式
        if (StringUtils.isNotBlank(email) && !RegexUtil.matchEmail(email)) {
            return Result.createByErrorMessage("邮箱格式不正确");
        }
        // 校验电话格式
        if (StringUtils.isNotBlank(phone) && !RegexUtil.matchMobile(phone)) {
            return Result.createByErrorMessage("电话号码格式不正确");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        // 检查邮箱是否已被注册
        if (StringUtils.isNotBlank(email) && !user.getEmail().equals(email)) {
            int emailCount = userMapper.countByEmail(email);
            if (emailCount > 0) {
                return Result.createByErrorMessage("该邮箱已被注册");
            }
            updateUser.setEmail(email);
        }
        // 检查电话是否已被注册
        if (StringUtils.isNotBlank(phone) && !user.getPhone().equals(phone)) {
            int phoneCount = userMapper.countByPhone(phone);
            if (phoneCount > 0) {
                return Result.createByErrorMessage("该电话已被注册");
            }
            updateUser.setPhone(phone);
        }

        if (StringUtils.isNotBlank(question)) {
            updateUser.setQuestion(question);
        }
        if (StringUtils.isNotBlank(answer)) {
            updateUser.setAnswer(answer);
        }

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) {
            if (StringUtils.isNotBlank(email)) {
                user.setEmail(email);
            }
            if (StringUtils.isNotBlank(phone)) {
                user.setPhone(phone);
            }
            if (StringUtils.isNotBlank(question)) {
                user.setQuestion(question);
            }
            if (StringUtils.isNotBlank(answer)) {
                user.setQuestion(answer);
            }
            return Result.createBySuccess("更新个人信息成功", user);
        }
        return Result.createByErrorMessage("更新个人信息失败");
    }

    @Override
    public User getCurrentUserInfo(Long userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public Result checkAdminRole(User user) {
        if (user != null && user.getRole() == Const.Role.ROLE_ADMIN) {
            return Result.createBySuccess();
        }
        return Result.createByError();
    }

    @Override
    public UserInfoVO assembleUserInfoVO(User user) {
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfoVO);
        return userInfoVO;
    }

    @Override
    public Result<String> checkRegisterParam(String param, String type) {
        if (Const.ValidType.USERNAME.equals(type)) {
            // 校验用户名格式
            if (!RegexUtil.matchUsername(param)) {
                return Result.createByErrorMessage("用户名格式不正确，需为4到16位的字母、数字、下划线或减号");
            }
            // 检查用户名是否已被注册
            int countUsername = userMapper.countByUsername(param);
            if (countUsername > 0) {
                return Result.createByErrorMessage("用户名已存在");
            }
        } else if (Const.ValidType.PASSWORD.equals(type)) {
            // 校验密码格式
            if (!RegexUtil.matchPassword(param)) {
                return Result.createByErrorMessage("密码格式不正确，需以字母开头，长度在6~18之间的非空字符");
            }
        } else if (Const.ValidType.EMAIL.equals(type)) {
            // 校验邮箱格式
            if (!RegexUtil.matchEmail(param)) {
                return Result.createByErrorMessage("邮箱格式不正确");
            }
            // 检查邮箱是否已被注册
            int emailCount = userMapper.countByEmail(param);
            if (emailCount > 0) {
                return Result.createByErrorMessage("该邮箱已被注册");
            }
        } else if (Const.ValidType.PHONE.equals(type)) {
            // 校验电话格式
            if (!RegexUtil.matchMobile(param)) {
                return Result.createByErrorMessage("电话号码格式不正确");
            }
            // 检查电话是否已被注册
            int phoneCount = userMapper.countByPhone(param);
            if (phoneCount > 0) {
                return Result.createByErrorMessage("该电话已被注册");
            }
        } else {
            return Result.createBySuccessMessage("校验类型不存在");
        }
        return Result.createBySuccessMessage("校验成功");
    }
}
