package cn.waynechu.mmall.mapper;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author waynechu
 * Created 2018-05-12 16:35
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRoleMapperTest {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Test
    public void getRoleIdsByUserId() {
        List<Long> roleIds = userRoleMapper.getRoleIdsByUserId(1L);
        Assert.assertEquals(3, roleIds.size());
    }
}