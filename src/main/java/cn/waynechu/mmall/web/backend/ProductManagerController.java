package cn.waynechu.mmall.web.backend;

import cn.waynechu.mmall.common.Const;
import cn.waynechu.mmall.common.Result;
import cn.waynechu.mmall.common.ResultEnum;
import cn.waynechu.mmall.entity.Product;
import cn.waynechu.mmall.properties.FtpServerProperties;
import cn.waynechu.mmall.service.FileService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author waynechu
 * Created 2018-05-23 13:06
 */
@Api(tags = "后台-商品管理接口")
@RestController
@RequestMapping("/v1/manager/product")
public class ProductManagerController {

    @Autowired
    private FtpServerProperties ftpServerProperties;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @ApiOperation(value = "新增/更新产品信息")
    @PostMapping("/save.do")
    public Result productSave(Product product) {
        return productService.saveOrUpdateProduct(product);
    }

    @ApiOperation(value = "商品上下架")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "商品id", paramType = "query", required = true),
            @ApiImplicitParam(name = "status", value = "商品状态：0下架，1在售，2删除", paramType = "query", required = true)
    })
    @PostMapping("/set_sale_status.do")
    public Result setSaleStatus(@RequestParam Long productId, @RequestParam Integer status) {
        return productService.setSaleStatus(productId, status);
    }

    @ApiOperation(value = "获取商品详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "产品id", paramType = "query", required = true)
    })
    @GetMapping("/detail.do")
    public Result<ProductDetialVO> getProductDetail(@RequestParam Long productId) {
        return productService.managerProductDetail(productId);
    }

    @ApiOperation(value = "获取商品列表", notes = "排序字段默认按id升序排序，若要降序则为id desc")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页数", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "orderBy", value = "排序字段", paramType = "query", defaultValue = "id")
    })
    @GetMapping("/list.do")
    public Result<PageInfo> getList(@RequestParam(defaultValue = "1") int pageNum,
                                    @RequestParam(defaultValue = "10") int pageSize,
                                    @RequestParam(defaultValue = "id") String orderBy) {
        return productService.getProductList(pageNum, pageSize, orderBy);
    }

    @ApiOperation(value = "商品名称模糊查询", notes = "排序字段默认按id升序排序，若要降序则为id desc")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keywords", value = "查询关键字", paramType = "query", required = true),
            @ApiImplicitParam(name = "pageNum", value = "页数", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "orderBy", value = "排序字段", paramType = "query", defaultValue = "id")
    })
    @GetMapping("/search.do")
    public Result<PageInfo> productSearch(@RequestParam String keywords,
                                          @RequestParam(defaultValue = "1") int pageNum,
                                          @RequestParam(defaultValue = "10") int pageSize,
                                          @RequestParam(defaultValue = "id") String orderBy) {
        return productService.searchProduct(keywords, pageNum, pageSize, orderBy);
    }

    @ApiOperation(value = "文件上传")
    @PostMapping(value = "/upload.do")
    public Result upload(@RequestParam(value = "upload_file") MultipartFile file, HttpServletRequest request) {
        String path = request.getSession().getServletContext().getRealPath("upload");
        // 上传后生成的文件名
        String targetFileName = fileService.upload(file, path);
        // FTP文件路径
        String url = ftpServerProperties.getUrlPrefix() + targetFileName;

        HashMap<String, String> fileMap = new HashMap<>();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);
        return Result.createBySuccess(fileMap);
    }

    @ApiOperation(value = "富文本文件上传")
    @PostMapping(value = "/richtext_img_upload.do")
    public Map richTextImgUpload(@RequestParam(value = "upload_file") MultipartFile file,
                                 HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        UserInfoVO user = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        HashMap<String, Object> resultMap = new HashMap<>();
        if (user == null) {
            resultMap.put("success", false);
            resultMap.put("msg", "请登录管理员");
            return resultMap;
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");
            // 上传后生成的文件名
            String targetFileName = fileService.upload(file, path);
            if (targetFileName.isEmpty()) {
                resultMap.put("success", false);
                resultMap.put("msg", "上传失败");
                return resultMap;
            }

            // FTP文件路径
            String url = ftpServerProperties.getUrlPrefix() + targetFileName;
            resultMap.put("success", true);
            resultMap.put("msg", "上传成功");
            resultMap.put("file_path", url);
            response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
            return resultMap;
        } else {
            resultMap.put("success", false);
            resultMap.put("msg", "无权限操作");
            return resultMap;
        }
    }
}
