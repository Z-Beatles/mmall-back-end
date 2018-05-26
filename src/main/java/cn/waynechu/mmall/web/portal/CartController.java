package cn.waynechu.mmall.web.portal;

import cn.waynechu.mmall.common.Const;
import cn.waynechu.mmall.common.ResponseCode;
import cn.waynechu.mmall.common.ServerResponse;
import cn.waynechu.mmall.service.CartService;
import cn.waynechu.mmall.vo.CartVO;
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
 * Created 2018-05-26 11:05
 */
@Api(tags = "门户-购物车接口")
@RestController
@RequestMapping("/v1/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @ApiOperation(value = "添加商品到购物车")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "商品id", paramType = "query", required = true),
            @ApiImplicitParam(name = "count", value = "购买数量", paramType = "query", required = true)
    })
    @PostMapping("/add.do")
    public ServerResponse<CartVO> add(@RequestParam Integer productId,
                                      @RequestParam Integer count,
                                      HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.add(currentUser.getId(), productId, count);
    }

    @ApiOperation(value = "更新购物车中指定商品的数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "商品id", paramType = "query", required = true),
            @ApiImplicitParam(name = "count", value = "更新后的数量", paramType = "query", required = true)
    })
    @PostMapping("/update.do")
    public ServerResponse<CartVO> update(@RequestParam Integer productId,
                                         @RequestParam Integer count,
                                         HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.update(currentUser.getId(), productId, count);
    }

    @ApiOperation(value = "删除购物车中的指定商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productIds", value = "商品id列表，若多个则用英文逗号分割", paramType = "query", required = true)
    })
    @DeleteMapping("/delete.do")
    public ServerResponse<CartVO> delete(@RequestParam String productIds,
                                         HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.delete(currentUser.getId(), productIds);
    }

    @ApiOperation(value = "获取购物车列表")
    @GetMapping("/list.do")
    public ServerResponse<CartVO> list(HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.list(currentUser.getId());
    }

    @ApiOperation(value = "获取购物车中的商品数量", notes = "返回的是购物车中的总商品数量，注意不是分类数量")
    @GetMapping("/get_cart_product_count.do")
    public ServerResponse<Integer> getCartProductCount(HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createBySuccess(0);
        }
        return cartService.getCartProductCount(currentUser.getId());
    }

    @ApiOperation(value = "购物车全选")
    @PostMapping("/select_all.do")
    public ServerResponse<CartVO> selectAll(HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.selectOrUnSelect(currentUser.getId(), null, Const.CartStatus.CHECKED);
    }


    @ApiOperation(value = "购物车全不选")
    @PostMapping("/un_select_all.do")
    public ServerResponse<CartVO> unSelectAll(HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.selectOrUnSelect(currentUser.getId(), null, Const.CartStatus.UN_CHECKED);
    }

    @ApiOperation(value = "购物车勾选指定商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "商品id", paramType = "query", required = true)
    })
    @PostMapping("/select.do")
    public ServerResponse<CartVO> select(@RequestParam Integer productId, HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.selectOrUnSelect(currentUser.getId(), productId, Const.CartStatus.CHECKED);
    }

    @ApiOperation(value = "购物车取消勾选指定商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "商品id", paramType = "query", required = true)
    })
    @PostMapping("/un_select.do")
    public ServerResponse<CartVO> unSelect(@RequestParam Integer productId, HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.selectOrUnSelect(currentUser.getId(), productId, Const.CartStatus.UN_CHECKED);
    }

}
