package cn.waynechu.mmall.web;

import cn.waynechu.mmall.dto.Result;
import cn.waynechu.mmall.dto.UserDTO;
import cn.waynechu.mmall.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author waynechu
 * Created 2018-05-12 00:42
 */
@RestController
@RequestMapping("/v1/users")
@Api(tags = "用户资源接口")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @ResponseBody
    @ApiOperation(value = "用户注册", notes = "注册成功返回用户id", produces = "application/json")
    public Result<UserDTO> register(
            @ApiParam(name = "username", value = "用户名", required = true)
            @RequestParam String username,
            @ApiParam(name = "password", value = "密码", required = true)
            @RequestParam String password) {
        UserDTO userDTO = userService.insertUser(username, password);
        return Result.createBySuccess(userDTO);
    }

    @GetMapping("/me")
    @ResponseBody
    @ApiOperation(value = "获取当前用户的个人信息", produces = "application/json")
    public Result<UserDTO> getUserInfo() {
        Long currentUserId = userService.getCurrentUserId();
        UserDTO userDTO = userService.getUserByUserId(currentUserId);
        return Result.createBySuccess(userDTO);
    }
}
