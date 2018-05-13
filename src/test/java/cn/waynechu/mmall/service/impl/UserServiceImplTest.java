package cn.waynechu.mmall.service.impl;

import cn.waynechu.mmall.ShiroTestHelper;
import cn.waynechu.mmall.dto.UserDTO;
import cn.waynechu.mmall.entity.User;
import cn.waynechu.mmall.service.UserService;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 * @author waynechu
 * Created 2018-05-13 12:18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Before
    public void setUp() {
        User user = new User();
        user.setId(1L);
        user.setUsername("waynechu");
        ShiroTestHelper.mockSubject(user);
    }

    @After
    public void tearDown() {
        ShiroTestHelper.clearSubject();
    }

    @Transactional
    @Test
    public void insertUser() {
        UserDTO userDTO = userService.insertUser("userForTest", "userForTest");
        Assert.assertNotNull(userDTO);
    }

    @Test
    public void getCurrentUserId() {
        Long currentUserId = userService.getCurrentUserId();
        Assert.assertEquals(1L, currentUserId.longValue());
    }

    @Test
    public void getUserByUserId() {
        UserDTO userDTO = userService.getUserByUserId(1L);
        Assert.assertNotNull(userDTO);
    }
}