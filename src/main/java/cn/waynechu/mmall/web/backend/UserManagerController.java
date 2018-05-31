package cn.waynechu.mmall.web.backend;

import cn.waynechu.mmall.common.Const;
import cn.waynechu.mmall.common.ServerResponse;
import cn.waynechu.mmall.service.UserService;
import cn.waynechu.mmall.util.CookieUtil;
import cn.waynechu.mmall.vo.UserInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

/**
 * @author waynechu
 * Created 2018-05-22 15:58
 */
@Api(tags = "后台-用户接口")
@RestController
@RequestMapping("/v1/manager/user")
public class UserManagerController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, Object> fastJsonRedisTemplate;

    @ApiOperation(value = "后台用户登录", notes = "登陆成功后返回用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", required = true),
            @ApiImplicitParam(name = "password", value = "密码", paramType = "query", required = true)
    })
    @PostMapping(value = "/login.do")
    public ServerResponse<UserInfoVO> login(@RequestParam String username, String password,
                                            HttpSession session, HttpServletRequest httpServletRequest,
                                            HttpServletResponse httpServletResponse) {
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (loginToken != null) {
            UserInfoVO currentUser = (UserInfoVO) fastJsonRedisTemplate.opsForValue().get(loginToken);
            if (currentUser != null) {
                return ServerResponse.createBySuccessMessage("用户已登录");
            }
        }
        ServerResponse<UserInfoVO> response = userService.login(username, password);
        if (response.isSuccess()) {
            UserInfoVO user = response.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                // 说明登录的是管理员
                //session.setAttribute(Const.CURRENT_USER, user);
                CookieUtil.writeLoginToke(httpServletResponse, session.getId());
                fastJsonRedisTemplate.opsForValue().set(session.getId(), response.getData(), 30, TimeUnit.MINUTES);
                return response;
            } else {
                return ServerResponse.createByErrorMessage("无管理员权限");
            }
        }
        return response;
    }
}
