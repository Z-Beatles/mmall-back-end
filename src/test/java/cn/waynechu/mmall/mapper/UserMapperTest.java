package cn.waynechu.mmall.mapper;

import cn.waynechu.mmall.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author waynechu
 * Created 2018-05-12 16:35
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void getByUserName() {
        User user = userMapper.getByUsername("waynechu");
        Assert.assertEquals(1L, user.getId().longValue());
    }
}