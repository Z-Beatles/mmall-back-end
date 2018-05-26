package cn.waynechu.mmall.web.portal;

import cn.waynechu.mmall.common.Const;
import cn.waynechu.mmall.common.ResponseCode;
import cn.waynechu.mmall.common.ServerResponse;
import cn.waynechu.mmall.entity.Shipping;
import cn.waynechu.mmall.service.ShippingService;
import cn.waynechu.mmall.vo.UserInfoVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author waynechu
 * Created 2018-05-26 22:40
 */
@Api(tags = "门户-收获地址接口")
@RestController
@RequestMapping("/v1/shipping")
public class ShippingController {

    @Autowired
    private ShippingService shippingService;

    @ApiOperation(value = "查看指定收获地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shippingId", value = "收货地址id", paramType = "query")
    })
    @GetMapping("/select.do")
    public ServerResponse<Shipping> select(@RequestParam Integer shippingId, HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return shippingService.select(currentUser.getId(), shippingId);
    }

    @ApiOperation(value = "添加收获地址")
    @PostMapping("/add.do")
    public ServerResponse add(Shipping shipping, HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return shippingService.add(currentUser.getId(), shipping);
    }

    @ApiOperation(value = "删除指定收获地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shippingId", value = "收货地址id", paramType = "query")
    })
    @DeleteMapping("/del.do")
    public ServerResponse del(@RequestParam Integer shippingId, HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return shippingService.del(currentUser.getId(), shippingId);
    }

    @ApiOperation(value = "更新收获地址")
    @PostMapping("/update.do")
    public ServerResponse update(Shipping shipping, HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return shippingService.update(currentUser.getId(), shipping);
    }

    @ApiOperation(value = "获取收获地址列表", notes = "排序字段默认按地址id升序排序，若要降序则为id desc")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页数", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "orderBy", value = "排序字段", paramType = "query", defaultValue = "id")
    })
    @GetMapping("/list.do")
    public ServerResponse<PageInfo> list(@RequestParam(defaultValue = "1") int pageNum,
                                         @RequestParam(defaultValue = "10") int pageSize,
                                         @RequestParam(defaultValue = "id") String orderBy,
                                         HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return shippingService.list(currentUser.getId(), pageNum, pageSize, orderBy);
    }

}
