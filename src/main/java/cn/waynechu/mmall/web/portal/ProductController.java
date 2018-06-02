package cn.waynechu.mmall.web.portal;

import cn.waynechu.mmall.common.Result;
import cn.waynechu.mmall.service.ProductService;
import cn.waynechu.mmall.vo.ProductDetialVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author waynechu
 * Created 2018-05-24 20:18
 */
@Api(tags = "门户-商品查询接口")
@RestController
@RequestMapping("/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @ApiOperation(value = "根据商品id获取商品详情")
    @ApiImplicitParam(name = "productId", value = "产品id", required = true, paramType = "path")
    @GetMapping("/{productId}")
    public Result<ProductDetialVO> getDetail(@PathVariable Long productId) {
        return productService.getProductDetail(productId);
    }

    @ApiOperation(value = "获取商品列表", notes = "根据关键字(keyword)或分类id(categoryId)获取商品列表，排序字段默认按商品id升序排序，若要降序则为id desc")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query"),
            @ApiImplicitParam(name = "categoryId", value = "分类id", paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "页数", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "orderBy", value = "排序字段", paramType = "query", defaultValue = "id")
    })
    @GetMapping
    public Result<PageInfo> listProducts(String keyword, Long categoryId,
                                         @RequestParam(defaultValue = "1") int pageNum,
                                         @RequestParam(defaultValue = "10") int pageSize,
                                         @RequestParam(defaultValue = "id") String orderBy) {
        return productService.getProductByKeywordAndCategoryId(keyword, categoryId, pageNum, pageSize, orderBy);
    }
}