package cn.waynechu.mmall.web.backend;

import cn.waynechu.mmall.common.Const;
import cn.waynechu.mmall.common.ResponseCode;
import cn.waynechu.mmall.common.ServerResponse;
import cn.waynechu.mmall.entity.Product;
import cn.waynechu.mmall.service.ProductService;
import cn.waynechu.mmall.service.UserService;
import cn.waynechu.mmall.vo.ProductDetialVO;
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
 * Created 2018-05-23 13:06
 */
@Api(tags = "商品后台管理接口")
@RestController
@RequestMapping("/v1/manager/product")
public class ProductManagerController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @ApiOperation(value = "新增/更新产品信息", produces = "application/json")
    @PostMapping("/save.do")
    public ServerResponse productSave(Product product,
                                      HttpSession session) {
        UserInfoVO user = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return productService.saveOrUpdateProduct(product);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @ApiOperation(value = "产品上下架", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "产品id", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "产品状态：0在架，1下架", paramType = "query", required = true)
    })
    @PostMapping("/set_sale_status.do")
    public ServerResponse setSaleStatus(@RequestParam Integer productId,
                                        @RequestParam Integer status,
                                        HttpSession session) {
        UserInfoVO user = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录管理员");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return productService.setSaleStatus(productId, status);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @ApiOperation(value = "获取产品详情", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "产品id", paramType = "query")
    })
    @GetMapping("/detail.do")
    public ServerResponse<ProductDetialVO> getProductDetail(@RequestParam Integer productId, HttpSession session) {
        UserInfoVO user = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录管理员");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return productService.getProductDetail(productId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @ApiOperation(value = "获取产品列表", notes = "排序字段默认按id升序排序，若要降序则为iddesc", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页数", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "orderBy", value = "排序字段", paramType = "query", defaultValue = "id")
    })
    @GetMapping("/list.do")
    public ServerResponse<PageInfo> getList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                            @RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
                                            HttpSession session) {
        UserInfoVO user = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录管理员");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return productService.getProductList(pageNum, pageSize, orderBy);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }
}
