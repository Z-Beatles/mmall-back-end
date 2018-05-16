package cn.waynechu.mmall.mapper;

import cn.waynechu.mmall.entity.User;

public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 通过邮箱获取用户信息
     *
     * @param email 邮箱
     * @return 用户信息
     */
    User getByEmail(String email);

    /**
     * 通过移动电话获取用户信息
     *
     * @param mobile 移动电话
     * @return 用户信息
     */
    User getByMobile(String mobile);

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getByUsername(String username);
}