package cn.waynechu.mmall.mapper;

import cn.waynechu.mmall.entity.Role;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author waynechu
 * Created 2018-05-12 16:34
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleMapperTest {

    @Autowired
    private RoleMapper roleMapper;

    @Test
    public void getRolesByRoleIds() {
        List<Long> roleIds = Arrays.asList(1L, 2L, 3L, 4L);
        List<Role> roles = roleMapper.getRolesByRoleIds(roleIds);
        Assert.assertEquals("admin", roles.get(0).getName());
    }
}