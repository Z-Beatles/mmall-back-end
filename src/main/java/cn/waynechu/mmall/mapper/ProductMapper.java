package cn.waynechu.mmall.mapper;

import cn.waynechu.mmall.entity.Product;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProductMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> listProducts();

    List<Product> serachProductByName(String concatProductName);

    List<Product> selectByNameAndCategoryIds(@Param("productName") String productName, @Param("categoryIdList") List<Long> categoryIdList);

    @MapKey("id")
    Map<Long, Product> mapProductByProductIds(@Param("productIds")List<Long> productIds);

    Integer selectStockByProductIdForUpdate(Long productId);
}