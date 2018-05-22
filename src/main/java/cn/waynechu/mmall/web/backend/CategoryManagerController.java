package cn.waynechu.mmall.web.backend;

import cn.waynechu.mmall.common.Const;
import cn.waynechu.mmall.common.ResponseCode;
import cn.waynechu.mmall.common.ServerResponse;
import cn.waynechu.mmall.entity.Category;
import cn.waynechu.mmall.service.CategoryService;
import cn.waynechu.mmall.service.UserService;
import cn.waynechu.mmall.vo.UserInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author waynechu
 * Created 2018-05-22 20:31
 */
@Api(tags = "商品分类后台管理接口")
@RestController
@RequestMapping("/v1/manager/category")
public class CategoryManagerController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "添加商品分类", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "父类id（默认0，顶级分类）", paramType = "query"),
            @ApiImplicitParam(name = "categoryName", value = "分类名称", paramType = "query", required = true)
    })
    @PostMapping("/add_category.do")
    public ServerResponse addCategory(@RequestParam(value = "parentId", defaultValue = "0") int parentId,
                                      @RequestParam String categoryName,
                                      HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "尚未登陆");
        }
        if (userService.checkAdminRole(currentUser).isSuccess()) {
            return categoryService.addCategory(parentId, categoryName);
        } else {
            return ServerResponse.createByErrorMessage("需要管理员权限");
        }
    }

    @ApiOperation(value = "修改商品分类名称", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId", value = "分类id", paramType = "query", required = true),
            @ApiImplicitParam(name = "categoryName", value = "分类名称", paramType = "query", required = true)
    })
    @PostMapping("/set_category_name.do")
    public ServerResponse updateCategoryName(@RequestParam Integer categoryId,
                                             @RequestParam String categoryName,
                                             HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "尚未登陆");
        }
        if (userService.checkAdminRole(currentUser).isSuccess()) {
            return categoryService.updateCategoryName(categoryId, categoryName);
        } else {
            return ServerResponse.createByErrorMessage("需要管理员权限");
        }
    }

    @ApiOperation(value = "获取下一级子分类列表", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId", value = "当前分类id", paramType = "query")
    })
    @GetMapping("/get_category.do")
    public ServerResponse<List<Category>> getChildrenParallelCategory(
            @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId,
            HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "尚未登陆");
        }
        if (userService.checkAdminRole(currentUser).isSuccess()) {
            return categoryService.getChildrenParallelCategory(categoryId);
        } else {
            return ServerResponse.createByErrorMessage("需要管理员权限");
        }
    }

    @ApiOperation(value = "获取当前分类id及递归子节点id", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId", value = "当前分类id", paramType = "query")
    })
    @GetMapping("/get_deep_category.do")
    public ServerResponse getCategoryAndDeepChildrenCategory(
            @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId,
            HttpSession session) {
        UserInfoVO currentUser = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "尚未登陆");
        }
        if (userService.checkAdminRole(currentUser).isSuccess()) {
            return categoryService.getCategoryAndChildrenById(categoryId);
        } else {
            return ServerResponse.createByErrorMessage("需要管理员权限");
        }
    }

}
