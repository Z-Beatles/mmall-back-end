package cn.waynechu.mmall.mapper;

import cn.waynechu.mmall.entity.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    List<Cart> selectByUserId(Long userId);

    /**
     * 获取购物车中未勾选的商品数
     *
     * @param userId 用户id
     * @return 未勾选的商品数
     */
    int countUnCheckedByUserId(Long userId);

    int deleteByUserIdAndProductIds(@Param("userId") Long userId, @Param("ids") List<Long> ids);

    /**
     * 设置用户购物车全选/全不选
     *
     * @param userId  用户id
     * @param checked 全选/全不选
     */
    void updateAllCheckedStatusByUserId(@Param("userId") Long userId, @Param("checked") int checked);

    void updateCheckedStatusByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId, @Param("checked") int checked);

    /**
     * 获取指定用户购物车中商品总数量
     *
     * @param userId 用户id
     * @return 商品总数量，用户id不存在返回0
     */
    int countCartProductByUserId(Long userId);

    /**
     * 获取指定用户勾选的商品列表
     *
     * @param userId 用户id
     * @return 购物车商品列表
     */
    List<Cart> listCheckedCartByUserId(Long userId);
}