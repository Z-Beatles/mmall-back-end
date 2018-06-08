package cn.waynechu.mmall.web.portal;

import cn.waynechu.mmall.common.Const;
import cn.waynechu.mmall.common.Result;
import cn.waynechu.mmall.entity.User;
import cn.waynechu.mmall.service.UserService;
import cn.waynechu.mmall.vo.UserInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author waynechu
 * Created 2018-05-21 19:40
 */
@Api(tags = "门户-用户接口")
@RestController
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户登录", notes = "登陆成功后返回用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", required = true),
            @ApiImplicitParam(name = "password", value = "密码", paramType = "query", required = true)
    })
    @PostMapping(value = "/login.do")
    public Result<UserInfoVO> login(@RequestParam String username,
                                    @RequestParam String password,
                                    HttpSession session) {
        User userInfo = (User) session.getAttribute(Const.CURRENT_USER);
        if (userInfo != null) {
            return Result.createBySuccessMessage("已登录，勿重复登录");
        }

        Result<User> response = userService.login(username, password);
        // 将用户信息保存到session中
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }

        // 组装用户VO
        UserInfoVO currentUser = userService.assembleUserInfoVO(response.getData());
        return Result.createBySuccess("登录成功", currentUser);
    }

    @ApiOperation(value = "获取用户信息", notes = "若用户未登录，返回错误(code:1)")
    @GetMapping(value = "/get_user_info.do")
    public Result<UserInfoVO> getUserInfo(HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser != null) {
            UserInfoVO userInfoVO = userService.assembleUserInfoVO(currentUser);
            return Result.createBySuccess(userInfoVO);
        }
        return Result.createByErrorMessage("用户未登录");
    }

    @ApiOperation(value = "获取当前用户信息，并强制登录", notes = "若用户未登录，会返回强制登录(code:1000)")
    @GetMapping(value = "/get_curr_user_info.do")
    public Result<UserInfoVO> getCurrentUserInfo(HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        User currentUserInfo = userService.getCurrentUserInfo(currentUser.getId());
        UserInfoVO userInfoVO = userService.assembleUserInfoVO(currentUserInfo);
        return Result.createBySuccess(userInfoVO);
    }

    @ApiOperation(value = "退出登录")
    @DeleteMapping(value = "/logout.do")
    public Result<String> logout(HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser != null) {
            session.removeAttribute(Const.CURRENT_USER);
            return Result.createBySuccessMessage("退出成功");
        }
        return Result.createByErrorMessage("尚未登录");
    }

    @ApiOperation(value = "用户注册")
    @PostMapping(value = "/register.do")
    public Result<String> register(@RequestParam String username, @RequestParam String password,
                                   String email, String phone, String question, String answer) {
        return userService.register(username, password, email, phone, question, answer);
    }

    @ApiOperation(value = "检查注册参数", notes = "用于异步检查用户名username、密码password、邮箱email、电话phone等注册参数，默认username")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param", value = "参数值", paramType = "query", required = true),
            @ApiImplicitParam(name = "type", value = "参数类型", defaultValue = "username", paramType = "query")
    })
    @GetMapping(value = "/check_valid.do")
    public Result<String> checkValid(@RequestParam String param,
                                     @RequestParam(required = false, defaultValue = "username") String type) {
        return userService.checkRegisterParam(param, type);
    }

    @ApiOperation(value = "忘记密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", required = true)
    })
    @GetMapping(value = "/forget_get_question.do")
    public Result<String> forgetGetQuestion(@RequestParam String username) {
        return userService.getQuestionByUsername(username);
    }

    @ApiOperation(value = "提交问题答案")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", required = true),
            @ApiImplicitParam(name = "question", value = "密保问题", paramType = "query", required = true),
            @ApiImplicitParam(name = "answer", value = "密保答案", paramType = "query", required = true)
    })
    @GetMapping(value = "/forget_check_answer.do")
    public Result<String> forgetCheckAnswer(@RequestParam String username,
                                            @RequestParam String question,
                                            @RequestParam String answer) {
        return userService.checkAnswer(username, question, answer);
    }

    @ApiOperation(value = "忘记密码的重设密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", required = true),
            @ApiImplicitParam(name = "passwordNew", value = "新密码", paramType = "query", required = true),
            @ApiImplicitParam(name = "forgetToken", value = "校验的token", paramType = "query", required = true)
    })
    @PostMapping(value = "/forget_reset_password.do")
    public Result<String> forgetRestPassword(@RequestParam String username,
                                             @RequestParam String passwordNew,
                                             @RequestParam String forgetToken) {
        return userService.forgetResetPassword(username, passwordNew, forgetToken);
    }

    @ApiOperation(value = "登录中状态更新密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "passwordOld", value = "旧密码", paramType = "query", required = true),
            @ApiImplicitParam(name = "passwordNew", value = "新密码", paramType = "query", required = true)
    })
    @PostMapping(value = "/update_password.do")
    public Result<String> resetPassword(@RequestParam String passwordOld,
                                        @RequestParam String passwordNew,
                                        HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        return userService.updatePassword(passwordOld, passwordNew, currentUser);
    }

    @ApiOperation(value = "登录状态下更新用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "邮箱", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "电话", paramType = "query"),
            @ApiImplicitParam(name = "question", value = "密保问题", paramType = "query"),
            @ApiImplicitParam(name = "answer", value = "密保答案", paramType = "query"),
    })
    @PostMapping(value = "/update_user_info.do")
    public Result updateUserInfo(String email, String phone, String question, String answer, HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        Result<User> response = userService.updateUserInfo(currentUser, email, phone, question, answer);
        if (response.isSuccess()) {
            // 同时更新session中信息
            response.getData().setUsername(currentUser.getUsername());
            session.setAttribute(Const.CURRENT_USER, response.getData());

            UserInfoVO userInfoVO = new UserInfoVO();
            BeanUtils.copyProperties(response.getData(), userInfoVO);
            return Result.createBySuccess("更新个人信息成功", userInfoVO);
        }
        return response;
    }
}
