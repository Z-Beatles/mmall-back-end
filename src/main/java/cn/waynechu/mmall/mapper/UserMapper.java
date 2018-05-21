package cn.waynechu.mmall.mapper;

import cn.waynechu.mmall.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @return 存在：1
     */
    int checkUsername(String username);

    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     * @return 存在：1
     */
    int checkEmail(String email);

    /**
     * 根据用户名和密码查找用户
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户
     */
    User selectLogin(@Param("username") String username, @Param("password") String password);

    /**
     * 根据用户名获取用户密保问题
     *
     * @param username 用户名
     * @return 密保问题
     */
    String getQuestionByUsername(String username);

    /**
     * 检查密码问题是否正确
     *
     * @param username 用户名
     * @param question 密保问题
     * @param answer   密保答案
     * @return 正确：1
     */
    int checkAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);
}