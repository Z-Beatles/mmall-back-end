package cn.waynechu.mmall.mapper;

import cn.waynechu.mmall.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author waynechu
 * Created 2018-05-12 15:42
 */
@Mapper
public interface UserMapper {

    /**
     * 根据用户名获取用户信息
     *
     * @param userName 用户名
     * @return 用户信息
     */
    User getByUsername(String userName);

    /**
     * 添加新用户
     *
     * @param user 用户信息
     * @return 受影响行数
     */
    Integer insertUser(User user);

    /**
     * 根据用户id获取用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    User getUserByUserId(Long userId);
}
