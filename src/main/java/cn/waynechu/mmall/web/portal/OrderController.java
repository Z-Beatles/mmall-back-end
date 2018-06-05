package cn.waynechu.mmall.web.portal;

import cn.waynechu.mmall.common.Const;
import cn.waynechu.mmall.common.Result;
import cn.waynechu.mmall.service.AlipayService;
import cn.waynechu.mmall.service.OrderService;
import cn.waynechu.mmall.vo.UserInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author waynechu
 * Created 2018-05-28 13:17
 */
@Slf4j
@Api(tags = "门户-订单接口")
@RestController
@RequestMapping("/v1/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AlipayService alipayService;

    @ApiOperation(value = "创建订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shippingId", value = "收货地址id", paramType = "query", required = true)
    })
    @PostMapping("/create.do")
    public Result create(Long shippingId, HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        return orderService.createOrder(currentUser.getId(), shippingId);
    }

    @ApiOperation(value = "取消订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderNo", value = "订单id", paramType = "query", required = true)
    })
    @DeleteMapping("/cancel.do")
    public Result cancel(Long orderNo, HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        return orderService.cancel(currentUser.getId(), orderNo);
    }

    @ApiOperation(value = "获取订单的商品信息（预下单）")
    @GetMapping("/get_order_cart_product.do")
    public Result getOrderCartProduct(HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        return orderService.getOrderCartProduct(currentUser.getId());
    }

    @ApiOperation(value = "获取订单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderNo", value = "订单id", paramType = "query", required = true)
    })
    @GetMapping("/detail.do")
    public Result detail(Long orderNo, HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        return orderService.getOrderDetail(currentUser.getId(), orderNo);
    }

    @ApiOperation(value = "获取订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页数", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", paramType = "query", defaultValue = "10")
    })
    @GetMapping("/list.do")
    public Result list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                       @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                       HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        return orderService.getOrderList(currentUser.getId(), pageNum, pageSize);
    }

    @ApiOperation(value = "支付订单", notes = "获取支付二维码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderNo", value = "订单号", paramType = "query", required = true)
    })
    @GetMapping("/pay.do")
    public Result pay(@RequestParam Long orderNo, HttpServletRequest request, HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);

        String path = request.getSession().getServletContext().getRealPath("upload");
        return alipayService.pay(orderNo, currentUser.getId(), path);
    }

    @ApiOperation(value = "查询订单支付状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderNo", value = "订单id", paramType = "query", required = true)
    })
    @GetMapping("/query_order_pay_status.do")
    public Result<Boolean> queryOrderPayStatus(Long orderNo, HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        Result result = orderService.queryOrderPayStatus(orderNo, currentUser.getId());
        if (result.isSuccess()) {
            return Result.createBySuccess(true);
        }
        return Result.createBySuccess(false);
    }
}
