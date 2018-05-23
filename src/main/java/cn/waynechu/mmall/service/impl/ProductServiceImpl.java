package cn.waynechu.mmall.service.impl;

import cn.waynechu.mmall.common.ServerResponse;
import cn.waynechu.mmall.entity.Category;
import cn.waynechu.mmall.entity.Product;
import cn.waynechu.mmall.mapper.CategoryMapper;
import cn.waynechu.mmall.mapper.ProductMapper;
import cn.waynechu.mmall.properties.FTPServerProperties;
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
    private FTPServerProperties ftpServerProperties;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
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
                    return ServerResponse.createBySuccessMessage("更新商品成功");
                }
                return ServerResponse.createByErrorMessage("更新商品失败");
            } else {
                // 新增产品
                int insertCount = productMapper.insert(product);
                return ServerResponse.createBySuccessMessage("新增产品成功");
            }
        } else {
            return ServerResponse.createByErrorMessage("新增或更新商品参数不正确");
        }
    }

    @Override
    public ServerResponse setSaleStatus(Integer productId, Integer status) {
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int updateCount = productMapper.updateByPrimaryKeySelective(product);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMessage("修改产品销售状态成功");
        }
        return ServerResponse.createByErrorMessage("修改产品销售状态失败");
    }

    @Override
    public ServerResponse<ProductDetialVO> getProductDetail(Integer productId) {
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product != null) {
            ProductDetialVO productDetialVO = new ProductDetialVO();
            BeanUtils.copyProperties(product, productDetialVO);

            // 设置图片前缀
            productDetialVO.setImageHost(ftpServerProperties.getUrlPrefix());

            // 设置父类id
            Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
            if (category == null) {
                // 默认为根节点
                productDetialVO.setParentCategoryId(0);
            } else {
                productDetialVO.setParentCategoryId(category.getParentId());
            }

            // 设置时间
            productDetialVO.setCreateTime(DateTimeUtil.toStringFromLocalDateTime(product.getCreateTime()));
            productDetialVO.setUpdateTime(DateTimeUtil.toStringFromLocalDateTime(product.getUpdateTime()));
            return ServerResponse.createBySuccess(productDetialVO);
        }
        return ServerResponse.createByErrorMessage("该商品不存在");
    }

    @Override
    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize, String orderBy) {
        PageHelper.startPage(pageNum, pageSize, orderBy);
        List<Product> productList = productMapper.listProducts();

        List<ProductListVO> productListVOS = assembleProductListVO(productList);
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVOS);
        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse<PageInfo> searchProduct(String keywords, int pageNum, int pageSize, String orderBy) {
        PageHelper.startPage(pageNum, pageSize, orderBy);
        List<Product> productList = productMapper.serachProductByName("%" + keywords + "%");

        List<ProductListVO> productListVOS = assembleProductListVO(productList);
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVOS);
        return ServerResponse.createBySuccess(pageInfo);
    }

    /**
     * 组装VO列表
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
