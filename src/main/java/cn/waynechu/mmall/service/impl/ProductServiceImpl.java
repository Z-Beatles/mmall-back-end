package cn.waynechu.mmall.service.impl;

import cn.waynechu.mmall.common.Const;
import cn.waynechu.mmall.common.ResultEnum;
import cn.waynechu.mmall.common.Result;
import cn.waynechu.mmall.entity.Category;
import cn.waynechu.mmall.entity.Product;
import cn.waynechu.mmall.mapper.CategoryMapper;
import cn.waynechu.mmall.mapper.ProductMapper;
import cn.waynechu.mmall.properties.FtpServerProperties;
import cn.waynechu.mmall.service.CategoryService;
import cn.waynechu.mmall.service.ProductService;
import cn.waynechu.mmall.util.DateTimeUtil;
import cn.waynechu.mmall.vo.ProductDetialVO;
import cn.waynechu.mmall.vo.ProductListVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author waynechu
 * Created 2018-05-23 14:28
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private FtpServerProperties ftpServerProperties;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryService categoryService;

    @Override
    public Result saveOrUpdateProduct(Product product) {
        if (product != null) {
            if (!product.getSubImages().isEmpty()) {
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }
            }
            if (product.getId() != null) {
                // 更新产品
                int updateCount = productMapper.updateByPrimaryKey(product);
                if (updateCount > 0) {
                    return Result.createBySuccessMessage("更新商品成功");
                }
                return Result.createByErrorMessage("更新商品失败");
            } else {
                // 新增产品
                int insertCount = productMapper.insert(product);
                return Result.createBySuccessMessage("新增产品成功");
            }
        } else {
            return Result.createByErrorMessage("新增或更新商品参数不正确");
        }
    }

    @Override
    public Result setSaleStatus(Long productId, Integer status) {
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int updateCount = productMapper.updateByPrimaryKeySelective(product);
        if (updateCount > 0) {
            return Result.createBySuccessMessage("修改产品销售状态成功");
        }
        return Result.createByErrorMessage("修改产品销售状态失败");
    }

    @Override
    public Result<ProductDetialVO> managerProductDetail(Long productId) {
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return Result.createByErrorMessage("该商品不存在");
        }
        ProductDetialVO productDetialVO = assembleProductDetailVO(product);
        return Result.createBySuccess(productDetialVO);
    }

    @Override
    public Result<PageInfo> getProductList(int pageNum, int pageSize, String orderBy) {
        PageHelper.startPage(pageNum, pageSize, orderBy);
        List<Product> productList = productMapper.listProducts();

        List<ProductListVO> productListVOS = assembleProductListVO(productList);
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVOS);
        return Result.createBySuccess(pageInfo);
    }

    @Override
    public Result<PageInfo> searchProduct(String keywords, int pageNum, int pageSize, String orderBy) {
        PageHelper.startPage(pageNum, pageSize, orderBy);
        List<Product> productList = productMapper.serachProductByName("%" + keywords + "%");

        List<ProductListVO> productListVOS = assembleProductListVO(productList);
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVOS);
        return Result.createBySuccess(pageInfo);
    }

    @Override
    public Result<ProductDetialVO> getProductDetail(Long productId) {
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return Result.createByErrorMessage("该商品不存在");
        }
        if (product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()) {
            return Result.createByErrorMessage("该商品已下架或者删除");
        }
        ProductDetialVO productDetialVO = assembleProductDetailVO(product);
        return Result.createBySuccess(productDetialVO);
    }

    @Override
    public Result<PageInfo> getProductByKeywordCategory(String keyword, Long categoryId, int pageNum, int pageSize, String orderBy) {
        if (keyword == null && categoryId == null) {
            return Result.createByErrorCodeMessage(ResultEnum.MISSING_REQUEST_PARAMETER.getCode(), ResultEnum.MISSING_REQUEST_PARAMETER.getMsg());
        }

        // 递归获取categoryId下所有子分类id
        List<Long> categoryIdList = new ArrayList<>();
        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && keyword == null) {
                // 没有该分类，并且还没有关键字，这个时候返回一个空的结果集，不报错
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVO> productListVoList = new ArrayList<>();
                PageInfo pageInfo = new PageInfo(productListVoList);
                return Result.createBySuccess(pageInfo);
            }
            categoryIdList = categoryService.getCategoryAndChildrenById(categoryId).getData();
        }

        // 拼接关键字
        if (keyword != null) {
            keyword = "%" + keyword + "%";
        }
        PageHelper.startPage(pageNum, pageSize, orderBy);
        List<Product> productList = productMapper.selectByNameAndCategoryIds(keyword, categoryIdList.size() == 0 ? null : categoryIdList);

        List<ProductListVO> productListVoList = assembleProductListVO(productList);
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return Result.createBySuccess(pageInfo);
    }

    /**
     * 组装ProductDetailVO
     */
    private ProductDetialVO assembleProductDetailVO(Product product) {
        ProductDetialVO productDetialVO = new ProductDetialVO();
        BeanUtils.copyProperties(product, productDetialVO);

        // 设置图片前缀
        productDetialVO.setImageHost(ftpServerProperties.getUrlPrefix());

        // 设置父类id
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            // 默认为根节点
            productDetialVO.setParentCategoryId(0L);
        } else {
            productDetialVO.setParentCategoryId(category.getParentId());
        }

        // 设置时间
        productDetialVO.setCreateTime(DateTimeUtil.toStringFromLocalDateTime(product.getCreateTime()));
        productDetialVO.setUpdateTime(DateTimeUtil.toStringFromLocalDateTime(product.getUpdateTime()));
        return productDetialVO;
    }

    /**
     * 组装ProductListVO列表
     */
    private List<ProductListVO> assembleProductListVO(List<Product> productList) {
        List<ProductListVO> productListVOS = new ArrayList<>();
        ProductListVO productListVO;
        for (Product product : productList) {
            productListVO = new ProductListVO();
            BeanUtils.copyProperties(product, productListVO);

            productListVO.setImageHost(ftpServerProperties.getUrlPrefix());
            productListVOS.add(productListVO);
        }
        return productListVOS;
    }

}
