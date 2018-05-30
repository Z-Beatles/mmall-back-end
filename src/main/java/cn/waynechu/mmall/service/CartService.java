package cn.waynechu.mmall.service;

import cn.waynechu.mmall.common.ServerResponse;
import cn.waynechu.mmall.vo.CartVO;

/**
 * @author waynechu
 * Created 2018-05-26 11:08
 */
public interface CartService {

    /**
     * 获取购物车列表
     *
     * @param userId 用户id
     * @return 购物车列表
     */
    ServerResponse<CartVO> list(Long userId);

    /**
     * 添加购物车
     *
     * @param userId    用户id
     * @param productId 产品id
     * @param count     购买数量
     * @return 购物车信息
     */
    ServerResponse<CartVO> add(Long userId, Long productId, Integer count);

    /**
     * 更新购物车数量
     *
     * @param userId    用户id
     * @param productId 产品id
     * @param count     购买数量
     * @return 购物车信息
     */
    ServerResponse<CartVO> update(Long userId, Long productId, Integer count);

    /**
     * 删除购物车中的指定商品
     *
     * @param userId     用户id
     * @param productIds 产品id列表，用英文逗号分割
     * @return 购物车信息
     */
    ServerResponse<CartVO> delete(Long userId, String productIds);

    /**
     * 购物车商品勾选
     *
     * @param userId    用户id
     * @param productId 商品id
     * @param checked   选择状态
     * @return 购物车信息
     */
    ServerResponse<CartVO> selectOrUnSelect(Long userId, Long productId, int checked);

    /**
     * 获取购物车中商品总数量
     *
     * @param userId 用户id
     * @return 商品总数量
     */
    ServerResponse<Integer> getCartProductCount(Long userId);
}
