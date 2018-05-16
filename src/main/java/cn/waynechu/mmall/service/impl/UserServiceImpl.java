package cn.waynechu.mmall.service.impl;

import cn.waynechu.mmall.dto.UserDTO;
import cn.waynechu.mmall.emuns.ResultEnum;
import cn.waynechu.mmall.entity.User;
import cn.waynechu.mmall.exception.AppException;
import cn.waynechu.mmall.mapper.UserMapper;
import cn.waynechu.mmall.service.UserService;
import cn.waynechu.mmall.util.RegexUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author waynechu
 * Created 2018-05-12 17:43
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getByAccount(String account) {
        User user;
        if (RegexUtil.matchEmail(account)) {
            user = userMapper.getByEmail(account);
        } else if (RegexUtil.matchMobile(account)) {
            user = userMapper.getByMobile(account);
        } else {
            user = userMapper.getByUsername(account);
        }
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO insertUser(String username, String password) {
        // 检测用户名是否有效
        checkUsername(username);
        // 检测密码是否合法
        checkPassword(password);

        String algorithmName = "SHA-1";
        int hashIterations = 1024;
        RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
        String salt = randomNumberGenerator.nextBytes().toHex();
        String hexPassword = new SimpleHash("SHA-1", password, salt, hashIterations).toHex();

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPasswordHash(hexPassword);
        newUser.setPasswordSalt(salt);
        newUser.setPasswordAlgo(algorithmName);
        newUser.setPasswordIteration(hashIterations);
        Integer count = userMapper.insertSelective(newUser);
        if (count != 1) {
            throw new AppException(ResultEnum.REGISTER_FAILED_ERROR);
        }

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(newUser, userDTO);
        return userDTO;
    }

    @Override
    public Long getCurrentUserId() {
        Subject currentUser = SecurityUtils.getSubject();
        User principal = (User) currentUser.getPrincipal();
        if (principal == null) {
            throw new AppException(ResultEnum.NOT_LOGIN_ERROR);
        } else {
            return principal.getId();
        }
    }

    @Override
    public UserDTO getUserByUserId(Long userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    @Override
    public boolean checkUsername(String username) {
        // 用户名不合法
        if (!RegexUtil.matchUsername(username)) {
            throw new AppException(ResultEnum.INVALID_USERNAME_ERROR);
        }
        // 用户名已存在
        if (userMapper.getByUsername(username) != null) {
            throw new AppException(ResultEnum.USERNAME_EXIST_ERROR);
        }
        return true;
    }

    @Override
    public boolean checkEmail(String email) {
        // 邮箱不合法
        if (!RegexUtil.matchEmail(email)) {
            throw new AppException(ResultEnum.INVALID_EMAIL_ERROR);
        }
        // 邮箱已被注册
        if (userMapper.getByEmail(email) != null) {
            throw new AppException(ResultEnum.EMAIL_EXIST_ERROR);
        }
        return true;
    }

    @Override
    public boolean checkMobile(String mobile) {
        // 手机号不合法
        if (!RegexUtil.matchMobile(mobile)) {
            throw new AppException(ResultEnum.INVALID_MOBILE_ERROR);
        }
        // 手机号已被使用
        if (userMapper.getByMobile(mobile) != null) {
            throw new AppException(ResultEnum.MOBILE_EXIST_ERROR);
        }
        return true;
    }

    /**
     * 检查密码是否有效
     *
     * @param password 密码
     */
    private void checkPassword(String password) {
        // 密码不合法
        if (!RegexUtil.matchPassword(password)) {
            throw new AppException(ResultEnum.INVALID_PASSWORD_ERROR);
        }
    }
}
