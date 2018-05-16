package cn.waynechu.mmall.service.impl;

import cn.waynechu.mmall.dto.UserDTO;
import cn.waynechu.mmall.service.SessionService;
import org.apache.shiro.util.ThreadContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author waynechu
 * Created 2018-05-13 10:30
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SessionServiceImplTest {

    @Autowired
    private SessionService sessionService;

    @Resource
    private org.apache.shiro.mgt.SecurityManager securityManager;

    @Before
    public void setUp() {
        ThreadContext.bind(securityManager);
    }

    @Test
    public void doLogin() {
        UserDTO userDTO = sessionService.doLogin(null, "waynechu", "123456", false, null);
        Assert.assertEquals(userDTO.getEmail(), "waynechu1996@gmail.com");
    }

    @Test
    public void doLogout() {
        doLogin();
        Long userId = sessionService.doLogout();
        Assert.assertEquals(1L, userId.longValue());
    }
}