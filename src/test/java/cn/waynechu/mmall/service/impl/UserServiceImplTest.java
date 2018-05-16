package cn.waynechu.mmall.service.impl;

import cn.waynechu.mmall.ShiroTestHelper;
import cn.waynechu.mmall.dto.UserDTO;
import cn.waynechu.mmall.emuns.ResultEnum;
import cn.waynechu.mmall.entity.User;
import cn.waynechu.mmall.service.UserService;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @author waynechu
 * Created 2018-05-13 12:18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

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

    @Test
    public void getByAccount() {
        List<String> accounts = Arrays.asList("waynechu1996@gmail.com", "15500000000", "waynechu");
        User user;
        for (String account : accounts) {
            user = userService.getByAccount(account);
            Assert.assertEquals(1L, user.getId().longValue());
        }
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

    @Test
    public void checkUserName() {
        // 用户名不合法
        try {
            userService.checkUsername("way");
        } catch (Exception e) {
            Assert.assertEquals(ResultEnum.INVALID_USERNAME_ERROR.getMsg(), e.getMessage());
        }
        // 用户名已存在
        try {
            userService.checkUsername("waynechu");
        } catch (Exception e) {
            Assert.assertEquals(ResultEnum.USERNAME_EXIST_ERROR.getMsg(), e.getMessage());
        }
        // 合法用户名
        Assert.assertTrue(userService.checkUsername("waynechu111"));
    }

    @Test
    public void checkEmail() {
        // 邮箱不合法
        try {
            userService.checkEmail("111@.");
        } catch (Exception e) {
            Assert.assertEquals(ResultEnum.INVALID_EMAIL_ERROR.getMsg(), e.getMessage());
        }
        // 邮箱已被使用
        try {
            userService.checkEmail("waynechu1996@gmail.com");
        } catch (Exception e) {
            Assert.assertEquals(ResultEnum.EMAIL_EXIST_ERROR.getMsg(), e.getMessage());
        }
        // 合法邮箱
        Assert.assertTrue(userService.checkEmail("158237627@qq.com"));
    }

    @Test
    public void checkMobile() {
        // 手机号不合法
        try {
            userService.checkMobile("1000000");
        } catch (Exception e) {
            Assert.assertEquals(ResultEnum.INVALID_MOBILE_ERROR.getMsg(), e.getMessage());
        }
        // 手机号已被使用
        try {
            userService.checkMobile("15500000000");
        } catch (Exception e) {
            Assert.assertEquals(ResultEnum.MOBILE_EXIST_ERROR.getMsg(), e.getMessage());
        }
        // 合法手机号
        Assert.assertTrue(userService.checkMobile("15500000001"));
    }
}