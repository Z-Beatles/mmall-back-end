package cn.waynechu.mmall.web.portal;

import cn.waynechu.mmall.common.ServerResponse;
import cn.waynechu.mmall.service.ProductService;
import cn.waynechu.mmall.vo.ProductDetialVO;
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

/**
 * @author waynechu
 * Created 2018-05-24 20:18
 */
@Api(tags = "门户-商品查询接口")
@RestController
@RequestMapping("/v1/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @ApiOperation(value = "获取产品详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "产品id", paramType = "query", required = true)
    })
    @GetMapping("/detail.do")
    public ServerResponse<ProductDetialVO> getDetail(@RequestParam Long productId) {
        return productService.getProductDetail(productId);
    }

    @ApiOperation(value = "根据关键字及分类获取产品列表", notes = "排序字段默认按商品id升序排序，若要降序则为id desc")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query"),
            @ApiImplicitParam(name = "categoryId", value = "分类id", paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "页数", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "orderBy", value = "排序字段", paramType = "query", defaultValue = "id")
    })
    @GetMapping("/list.do")
    public ServerResponse<PageInfo> listProducts(@RequestParam(required = false) String keyword,
                                                 @RequestParam(required = false) Long categoryId,
                                                 @RequestParam(defaultValue = "1") int pageNum,
                                                 @RequestParam(defaultValue = "10") int pageSize,
                                                 @RequestParam(defaultValue = "id") String orderBy) {
        return productService.getProductByKeywordCategory(keyword, categoryId, pageNum, pageSize, orderBy);
    }
}
