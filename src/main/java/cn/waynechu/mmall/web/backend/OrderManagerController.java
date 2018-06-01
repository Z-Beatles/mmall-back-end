package cn.waynechu.mmall.web.backend;

import cn.waynechu.mmall.common.Const;
import cn.waynechu.mmall.common.ResultEnum;
import cn.waynechu.mmall.common.Result;
import cn.waynechu.mmall.service.OrderService;
import cn.waynechu.mmall.service.UserService;
import cn.waynechu.mmall.vo.OrderVO;
import cn.waynechu.mmall.vo.UserInfoVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author waynechu
 * Created 2018-05-29 12:58
 */
@Api(tags = "后台-订单管理接口")
@RestController
@RequestMapping("/v1/manager/order")
public class OrderManagerController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/list.do")
    public Result<PageInfo> orderList(@RequestParam(defaultValue = "1") int pageNum,
                                      @RequestParam(defaultValue = "10") int pageSize,
                                      @RequestParam(defaultValue = "create_time desc") String orderBy,
                                      HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return Result.createByErrorCodeMessage(ResultEnum.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (userService.checkAdminRole(currentUser).isSuccess()) {
            return orderService.manageList(pageNum, pageSize, orderBy);
        } else {
            return Result.createByErrorMessage("无权限操作");
        }
    }

    @GetMapping("/detail.do")
    public Result<OrderVO> orderDetail(Long orderNo, HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return Result.createByErrorCodeMessage(ResultEnum.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (userService.checkAdminRole(currentUser).isSuccess()) {
            return orderService.manageDetail(orderNo);
        } else {
            return Result.createByErrorMessage("无权限操作");
        }
    }

    @GetMapping("search.do")
    public Result<PageInfo> orderSearch(Long orderNo,
                                        @RequestParam(defaultValue = "1") int pageNum,
                                        @RequestParam(defaultValue = "10") int pageSize,
                                        HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return Result.createByErrorCodeMessage(ResultEnum.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (userService.checkAdminRole(currentUser).isSuccess()) {
            return orderService.manageSearch(orderNo, pageNum, pageSize);
        } else {
            return Result.createByErrorMessage("无权限操作");
        }
    }

    @GetMapping("/send_goods.do")
    public Result<String> orderSendGoods(Long orderNo, HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return Result.createByErrorCodeMessage(ResultEnum.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (userService.checkAdminRole(currentUser).isSuccess()) {
            return orderService.manageSendGoods(orderNo);
        } else {
            return Result.createByErrorMessage("无权限操作");
        }
    }

}
