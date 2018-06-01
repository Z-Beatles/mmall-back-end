package cn.waynechu.mmall.service;

import cn.waynechu.mmall.common.Result;
import cn.waynechu.mmall.entity.Product;
import cn.waynechu.mmall.vo.ProductDetialVO;
import com.github.pagehelper.PageInfo;

/**
 * @author waynechu
 * Created 2018-05-23 14:25
 */
public interface ProductService {

    Result saveOrUpdateProduct(Product product);

    Result setSaleStatus(Long productId, Integer status);

    Result<ProductDetialVO> managerProductDetail(Long productId);

    Result<PageInfo> getProductList(int pageNum, int pageSize, String orderBy);

    Result<PageInfo> searchProduct(String keywords, int pageNum, int pageSize, String orderBy);

    Result<ProductDetialVO> getProductDetail(Long productId);

    Result<PageInfo> getProductByKeywordCategory(String keyword, Long categoryId, int pageNum, int pageSize, String orderBy);
}
