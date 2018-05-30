package cn.waynechu.mmall.web.backend;

import cn.waynechu.mmall.common.Const;
import cn.waynechu.mmall.common.ResponseCode;
import cn.waynechu.mmall.common.ServerResponse;
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
    public ServerResponse productSave(Product product, HttpSession session) {
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

    @ApiOperation(value = "产品上下架")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "产品id", paramType = "query", required = true),
            @ApiImplicitParam(name = "status", value = "产品状态：0下架，1在售，2删除", paramType = "query", required = true)
    })
    @PostMapping("/set_sale_status.do")
    public ServerResponse setSaleStatus(@RequestParam Long productId,
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

    @ApiOperation(value = "获取产品详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "产品id", paramType = "query", required = true)
    })
    @GetMapping("/detail.do")
    public ServerResponse<ProductDetialVO> getProductDetail(@RequestParam Long productId, HttpSession session) {
        UserInfoVO user = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录管理员");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return productService.managerProductDetail(productId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @ApiOperation(value = "获取产品列表", notes = "排序字段默认按id升序排序，若要降序则为id desc")
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

    @ApiOperation(value = "商品名称模糊查询", notes = "排序字段默认按id升序排序，若要降序则为id desc")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keywords", value = "查询关键字", paramType = "query", required = true),
            @ApiImplicitParam(name = "pageNum", value = "页数", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "orderBy", value = "排序字段", paramType = "query", defaultValue = "id")
    })
    @GetMapping("/search.do")
    public ServerResponse<PageInfo> productSearch(@RequestParam String keywords,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                  @RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
                                                  HttpSession session) {
        UserInfoVO user = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录管理员");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return productService.searchProduct(keywords, pageNum, pageSize, orderBy);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @ApiOperation(value = "文件上传")
    @PostMapping(value = "/upload.do")
    public ServerResponse upload(@RequestParam(value = "upload_file") MultipartFile file,
                                 HttpServletRequest request,
                                 HttpSession session) {
        UserInfoVO user = (UserInfoVO) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录管理员");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");
            // 上传后生成的文件名
            String targetFileName = fileService.upload(file, path);
            // FTP文件路径
            String url = ftpServerProperties.getUrlPrefix() + targetFileName;

            HashMap<String, String> fileMap = new HashMap<>();
            fileMap.put("uri", targetFileName);
            fileMap.put("url", url);
            return ServerResponse.createBySuccess(fileMap);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @ApiOperation(value = "富文本文件上传")
    @PostMapping(value = "/richtext_img_upload.do")
    public Map richTextImgUpload(@RequestParam(value = "upload_file") MultipartFile file,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 HttpSession session) {
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
