package cn.waynechu.mmall.mapper;

import cn.waynechu.mmall.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author waynechu
 * Created 2018-05-12 14:41
 */
@Mapper
public interface RoleMapper {

    /**
     * 根据角色id列表获取角色列表
     *
     * @param roleIds  角色id列表
     * @return 角色列表
     */
    List<Role> getRolesByRoleIds(List<Long> roleIds);
}
