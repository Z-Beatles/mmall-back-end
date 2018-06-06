package cn.waynechu.mmall.mapper;

import cn.waynechu.mmall.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @return 存在：1
     */
    int countByUsername(String username);

    /**
     * 根据用户名和密码查找用户
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户
     */
    User selectByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    /**
     * 根据用户名获取用户密保问题
     *
     * @param username 用户名
     * @return 密保问题
     */
    String selectQuestionByUsername(String username);

    /**
     * 检查密码问题是否正确
     *
     * @param username 用户名
     * @param question 密保问题
     * @param answer   密保答案
     * @return 正确：1
     */
    int checkAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);

    /**
     * 更新用户密码
     *
     * @param username    用户名
     * @param md5Password 密码摘要
     * @return 受影响的行数
     */
    int updatePasswordByUsername(@Param("username") String username, @Param("md5Password") String md5Password);

    /**
     * 检查密码是否正确
     *
     * @param md5Password 密码摘要
     * @param userId      用户Id
     * @return 正确：1
     */
    int countByUserIdAndPassword(@Param("md5Password") String md5Password, @Param("userId") Long userId);

    /**
     * 判断邮箱是否已被注册
     *
     * @param email 邮箱
     * @return 已注册：1
     */
    int countByEmail(String email);

    /**
     * 判断电话是否已被注册
     *
     * @param phone 电话
     * @return  已注册：1
     */
    int countByPhone(String phone);
}