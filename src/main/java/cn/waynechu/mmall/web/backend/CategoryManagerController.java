package cn.waynechu.mmall.web.backend;

import cn.waynechu.mmall.common.Result;
import cn.waynechu.mmall.entity.Category;
import cn.waynechu.mmall.service.CategoryService;
import cn.waynechu.mmall.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author waynechu
 * Created 2018-05-22 20:31
 */
@Api(tags = "后台-商品分类接口")
@RestController
@RequestMapping("/v1/manager/category")
public class CategoryManagerController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "添加商品分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "父类id（默认0，顶级分类）", paramType = "query"),
            @ApiImplicitParam(name = "categoryName", value = "分类名称", paramType = "query", required = true)
    })
    @PostMapping("/add_category.do")
    public Result addCategory(@RequestParam(defaultValue = "0") Long parentId, @RequestParam String categoryName) {
        return categoryService.addCategory(parentId, categoryName);
    }

    @ApiOperation(value = "修改商品分类名称")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId", value = "分类id", paramType = "query", required = true),
            @ApiImplicitParam(name = "categoryName", value = "分类名称", paramType = "query", required = true)
    })
    @PostMapping("/set_category_name.do")
    public Result updateCategoryName(@RequestParam Long categoryId, @RequestParam String categoryName) {
        return categoryService.updateCategoryName(categoryId, categoryName);
    }

    @ApiOperation(value = "获取下一级子分类列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId", value = "当前分类id", paramType = "query")
    })
    @GetMapping("/get_category.do")
    public Result<List<Category>> getChildrenParallelCategory(@RequestParam(defaultValue = "0") Long categoryId) {
        return categoryService.getChildrenParallelCategory(categoryId);
    }

    @ApiOperation(value = "获取当前分类id及递归子节点id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId", value = "当前分类id", paramType = "query")
    })
    @GetMapping("/get_deep_category.do")
    public Result getCategoryAndDeepChildrenCategory(@RequestParam(defaultValue = "0") Long categoryId) {
        return categoryService.getCategoryAndChildrenById(categoryId);
    }
}
