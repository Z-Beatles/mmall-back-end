package cn.waynechu.mmall.web.portal;

import cn.waynechu.mmall.common.Const;
import cn.waynechu.mmall.common.ResponseCode;
import cn.waynechu.mmall.common.ServerResponse;
import cn.waynechu.mmall.service.UserService;
import cn.waynechu.mmall.vo.UserInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author waynechu
 * Created 2018-05-21 19:40
 */
@Api(tags = "用户接口")
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
    public ServerResponse<UserInfoVO> login(@RequestParam String username,
                                            @RequestParam String password,
                                            HttpSession session) {
        ServerResponse<UserInfoVO> response = userService.login(username, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    @ApiOperation(value = "获取当前用户信息，并强制登录")
    @GetMapping(value = "/get_information.do")
    public ServerResponse<UserInfoVO> getInformation(HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "尚未登录，需要强制登录");
        }
        return userService.getInformation(currentUser.getId());
    }

    @ApiOperation(value = "退出登录", notes = "")
    @DeleteMapping(value = "/logout.do")
    public ServerResponse<String> logout(HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser != null) {
            session.removeAttribute(Const.CURRENT_USER);
            return ServerResponse.createBySuccessMessage("退出成功");
        }
        return ServerResponse.createByErrorMessage("尚未登录");
    }

    @ApiOperation(value = "用户注册", notes = "")
    @PostMapping(value = "/register.do")
    public ServerResponse<String> register(@RequestParam String username,
                                           @RequestParam String password,
                                           @RequestParam(required = false) String email,
                                           @RequestParam(required = false) String phone,
                                           @RequestParam(required = false) String question,
                                           @RequestParam(required = false) String answer) {
        return userService.register(username, password, email, phone, question, answer);
    }

    @ApiOperation(value = "检查注册参数", notes = "用于检查用户名、邮箱等是否已经被注册，其中参数类型可以是：username, email，默认username")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "value", value = "参数值", paramType = "query", required = true),
            @ApiImplicitParam(name = "type", value = "参数类型", defaultValue = "username", paramType = "query")
    })
    @PostMapping(value = "/check_valid.do")
    public ServerResponse<String> checkValid(@RequestParam String value,
                                             @RequestParam(required = false, defaultValue = "username") String type) {
        return userService.checkValid(value, type);
    }


    @ApiOperation(value = "忘记密码", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", required = true)
    })
    @PostMapping(value = "/forget_get_question.do")
    public ServerResponse<String> forgetGetQuestion(@RequestParam String username) {
        return userService.selectQuestion(username);
    }

    @ApiOperation(value = "提交问题答案", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", required = true),
            @ApiImplicitParam(name = "question", value = "密保问题", paramType = "query", required = true),
            @ApiImplicitParam(name = "answer", value = "密保答案", paramType = "query", required = true)
    })
    @PostMapping(value = "/forget_check_answer.do")
    public ServerResponse<String> forgetCheckAnswer(@RequestParam String username,
                                                    @RequestParam String question,
                                                    @RequestParam String answer) {
        return userService.checkAnswer(username, question, answer);
    }

    @ApiOperation(value = "忘记密码的重设密码", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", required = true),
            @ApiImplicitParam(name = "passwordNew", value = "新密码", paramType = "query", required = true),
            @ApiImplicitParam(name = "forgetToken", value = "校验的token", paramType = "query", required = true)
    })
    @PostMapping(value = "/forget_reset_password.do")
    public ServerResponse<String> forgetRestPassword(@RequestParam String username,
                                                     @RequestParam String passwordNew,
                                                     @RequestParam String forgetToken) {
        return userService.forgetResetPassword(username, passwordNew, forgetToken);
    }

    @ApiOperation(value = "登录中状态重置密码", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "passwordOld", value = "原始密码", paramType = "query", required = true),
            @ApiImplicitParam(name = "passwordNew", value = "新密码", paramType = "query", required = true)
    })
    @PostMapping(value = "/reset_password.do")
    public ServerResponse<String> resetPassword(@RequestParam String passwordOld,
                                                @RequestParam String passwordNew,
                                                HttpSession session) {
        UserInfoVO userInfoVO = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (userInfoVO == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return userService.resetPassword(passwordOld, passwordNew, userInfoVO);
    }

    @ApiOperation(value = "登录状态下更新用户信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "邮箱", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "电话", paramType = "query"),
            @ApiImplicitParam(name = "question", value = "密保问题", paramType = "query"),
            @ApiImplicitParam(name = "answer", value = "密保答案", paramType = "query"),
    })
    @PostMapping(value = "/update_information.do")
    public ServerResponse<UserInfoVO> updateInformation(@RequestParam(required = false) String email,
                                                        @RequestParam(required = false) String phone,
                                                        @RequestParam(required = false) String question,
                                                        @RequestParam(required = false) String answer,
                                                        HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }

        ServerResponse<UserInfoVO> response = userService.updateInformation(currentUser, email, phone, question, answer);
        if (response.isSuccess()) {
            response.getData().setUsername(currentUser.getUsername());
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    @ApiOperation(value = "获取当前用户信息", notes = "")
    @GetMapping(value = "/get_user_info.do")
    public ServerResponse<UserInfoVO> getUserInfo(HttpSession session) {
        UserInfoVO userInfoVO = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (userInfoVO != null) {
            return ServerResponse.createBySuccess(userInfoVO);
        }
        return ServerResponse.createByErrorMessage("尚未登录");
    }
}
