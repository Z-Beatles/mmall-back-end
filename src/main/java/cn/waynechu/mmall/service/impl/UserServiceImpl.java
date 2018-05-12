package cn.waynechu.mmall.service.impl;

import cn.waynechu.mmall.dto.UserDTO;
import cn.waynechu.mmall.emuns.ResultEnum;
import cn.waynechu.mmall.entity.User;
import cn.waynechu.mmall.exception.AppException;
import cn.waynechu.mmall.mapper.UserMapper;
import cn.waynechu.mmall.service.UserService;
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
    public User getByUsername(String username) {
        return userMapper.getByUserName(username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO insertUser(String username, String password) {
        User user = userMapper.getByUserName(username);
        if (user != null) {
            throw new AppException(ResultEnum.ACCOUNT_EXIST_ERROR);
        }

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
        Integer count = userMapper.insertUser(newUser);
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
        User user = userMapper.getUserByUserId(userId);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }
}
