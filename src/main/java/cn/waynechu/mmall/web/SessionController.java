package cn.waynechu.mmall.web;

import cn.waynechu.mmall.dto.UserDTO;
import cn.waynechu.mmall.dto.Result;
import cn.waynechu.mmall.emuns.ResultEnum;
import cn.waynechu.mmall.service.SessionService;
import cn.waynechu.mmall.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author waynechu
 * Created 2018-05-12 17:37
 */
@RestController
@RequestMapping("/v1/sessions")
@Api(tags = "会话管理接口")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @PostMapping
    @ApiOperation(value = "用户登录", notes = "登录类型暂时可不填写，登陆成功返回用户id和一个名为‘sid’" +
            "的Cookie以及rememberMe Cookie有效期1年", produces = "application/json")
    public Result<UserDTO> loginAction(
            @ApiParam(name = "loginType", value = "登陆的类型")
            @RequestParam(required = false) String loginType,
            @ApiParam(name = "account", value = "账号", required = true)
            @RequestParam String account,
            @ApiParam(name = "password", value = "密码", required = true)
            @RequestParam String password,
            @ApiParam(name = "rememberMe", value = "记住我")
            @RequestParam(required = false, defaultValue = "true") boolean rememberMe,
            @ApiParam(name = "host", value = "登陆IP")
            @RequestParam(required = false) String host) {
        Subject currentUser = SecurityUtils.getSubject();
        if (!currentUser.isAuthenticated()) {
            UserDTO userId = sessionService.doLogin(loginType, account, password, rememberMe, host, currentUser);
            return ResultUtil.success(ResultEnum.LOGIN_SUCCEED_INFO, userId);
        }
        return ResultUtil.error(ResultEnum.REPEAT_LOGIN_ERROR);
    }

    @DeleteMapping
    @ApiOperation(value = "用户退出", produces = "application/json")
    public Result logoutAction() {
        return ResultUtil.success(ResultEnum.LOGOUT_SUCCEED_INFO, sessionService.doLogout());
    }
}
