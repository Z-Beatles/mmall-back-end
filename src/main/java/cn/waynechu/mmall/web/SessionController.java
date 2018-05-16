package cn.waynechu.mmall.web;

import cn.waynechu.mmall.dto.Result;
import cn.waynechu.mmall.dto.UserDTO;
import cn.waynechu.mmall.service.SessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
    @ApiOperation(value = "用户登录", notes = "登陆成功返回用户信息", produces = "application/json")
    public Result<UserDTO> loginAction(
            @ApiParam(name = "loginType", value = "登陆的类型")
            @RequestParam(required = false) String loginType,
            @ApiParam(name = "account", value = "帐号", required = true)
            @RequestParam String account,
            @ApiParam(name = "password", value = "密码", required = true)
            @RequestParam String password,
            @ApiParam(name = "rememberMe", value = "记住我")
            @RequestParam(required = false, defaultValue = "true") boolean rememberMe,
            @ApiParam(name = "host", value = "登陆IP")
            @RequestParam(required = false) String host) {
        UserDTO userDTO = sessionService.doLogin(loginType, account, password, rememberMe, host);
        return Result.createBySuccess(userDTO);
    }

    @DeleteMapping
    @ApiOperation(value = "用户退出", notes = "退出成功返回用户ID", produces = "application/json")
    public Result logoutAction() {
        Long userId = sessionService.doLogout();
        return Result.createBySuccess(userId);
    }
}
