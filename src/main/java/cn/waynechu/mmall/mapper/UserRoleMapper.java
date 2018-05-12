package cn.waynechu.mmall.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author waynechu
 * Created 2018-05-12 16:19
 */
@Mapper
public interface UserRoleMapper {

    /**
     * 根据用户id获取权限id列表
     * @param id 用户id
     * @return 权限id列表
     */
    List<Long> getRoleIdsByUserId(Long id);
}
