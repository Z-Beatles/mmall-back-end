package cn.waynechu.mmall.service;

import cn.waynechu.mmall.common.ServerResponse;
import cn.waynechu.mmall.entity.Product;
import cn.waynechu.mmall.vo.ProductDetialVO;
import com.github.pagehelper.PageInfo;

/**
 * @author waynechu
 * Created 2018-05-23 14:25
 */
public interface ProductService {

    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse setSaleStatus(Long productId, Integer status);

    ServerResponse<ProductDetialVO> managerProductDetail(Long productId);

    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize, String orderBy);

    ServerResponse<PageInfo> searchProduct(String keywords, int pageNum, int pageSize, String orderBy);

    ServerResponse<ProductDetialVO> getProductDetail(Long productId);

    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Long categoryId, int pageNum, int pageSize, String orderBy);
}
