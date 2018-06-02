package cn.waynechu.mmall.web.backend;

import cn.waynechu.mmall.common.Result;
import cn.waynechu.mmall.service.OrderService;
import cn.waynechu.mmall.service.UserService;
import cn.waynechu.mmall.vo.OrderVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value = "获取订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页数", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "orderBy", value = "排序字段", paramType = "query", defaultValue = "create_time desc")
    })
    @GetMapping("/list.do")
    public Result<PageInfo> orderList(@RequestParam(defaultValue = "1") int pageNum,
                                      @RequestParam(defaultValue = "10") int pageSize,
                                      @RequestParam(defaultValue = "create_time desc") String orderBy) {
        return orderService.manageList(pageNum, pageSize, orderBy);
    }

    @ApiOperation(value = "获取订单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderNo", value = "订单id", paramType = "query", required = true)
    })
    @GetMapping("/detail.do")
    public Result<OrderVO> orderDetail(@RequestParam Long orderNo) {
        return orderService.manageDetail(orderNo);
    }

    @ApiOperation(value = "搜索订单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页数", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", paramType = "query", defaultValue = "10")
    })
    @GetMapping("/search.do")
    public Result<PageInfo> orderSearch(@RequestParam Long orderNo, @RequestParam(defaultValue = "1") int pageNum,
                                        @RequestParam(defaultValue = "10") int pageSize) {
        return orderService.manageSearch(orderNo, pageNum, pageSize);
    }

    @ApiOperation(value = "订单发货")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderNo", value = "订单id", paramType = "query", required = true)
    })
    @GetMapping("/send_goods.do")
    public Result<String> orderSendGoods(@RequestParam Long orderNo) {
        return orderService.manageSendGoods(orderNo);
    }
}
