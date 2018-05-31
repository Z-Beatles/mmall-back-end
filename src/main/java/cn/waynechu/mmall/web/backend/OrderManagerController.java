package cn.waynechu.mmall.web.backend;

import cn.waynechu.mmall.common.ResponseCode;
import cn.waynechu.mmall.common.ServerResponse;
import cn.waynechu.mmall.service.OrderService;
import cn.waynechu.mmall.service.UserService;
import cn.waynechu.mmall.util.CookieUtil;
import cn.waynechu.mmall.vo.OrderVO;
import cn.waynechu.mmall.vo.UserInfoVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private RedisTemplate<String, Object> fastJsonRedisTemplate;

    @GetMapping("/list.do")
    public ServerResponse<PageInfo> orderList(@RequestParam(defaultValue = "1") int pageNum,
                                              @RequestParam(defaultValue = "10") int pageSize,
                                              @RequestParam(defaultValue = "create_time desc") String orderBy,
                                              HttpServletRequest httpServletRequest) {
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (loginToken == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        UserInfoVO currentUser = (UserInfoVO) fastJsonRedisTemplate.opsForValue().get(loginToken);

        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (userService.checkAdminRole(currentUser).isSuccess()) {
            return orderService.manageList(pageNum, pageSize, orderBy);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @GetMapping("/detail.do")
    public ServerResponse<OrderVO> orderDetail(Long orderNo,  HttpServletRequest httpServletRequest) {
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (loginToken == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        UserInfoVO currentUser = (UserInfoVO) fastJsonRedisTemplate.opsForValue().get(loginToken);

        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (userService.checkAdminRole(currentUser).isSuccess()) {
            return orderService.manageDetail(orderNo);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @GetMapping("/search.do")
    public ServerResponse<PageInfo> orderSearch(Long orderNo,
                                                @RequestParam(defaultValue = "1") int pageNum,
                                                @RequestParam(defaultValue = "10") int pageSize,
                                                HttpServletRequest httpServletRequest) {
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (loginToken == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        UserInfoVO currentUser = (UserInfoVO) fastJsonRedisTemplate.opsForValue().get(loginToken);

        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (userService.checkAdminRole(currentUser).isSuccess()) {
            return orderService.manageSearch(orderNo, pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @GetMapping("/send_goods.do")
    public ServerResponse<String> orderSendGoods(Long orderNo,  HttpServletRequest httpServletRequest) {
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (loginToken == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        UserInfoVO currentUser = (UserInfoVO) fastJsonRedisTemplate.opsForValue().get(loginToken);

        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (userService.checkAdminRole(currentUser).isSuccess()) {
            return orderService.manageSendGoods(orderNo);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

}
