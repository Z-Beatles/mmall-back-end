package cn.waynechu.mmall.service.impl;

import cn.waynechu.mmall.common.Const;
import cn.waynechu.mmall.common.Result;
import cn.waynechu.mmall.entity.Cart;
import cn.waynechu.mmall.entity.Product;
import cn.waynechu.mmall.mapper.CartMapper;
import cn.waynechu.mmall.mapper.ProductMapper;
import cn.waynechu.mmall.properties.FtpServerProperties;
import cn.waynechu.mmall.service.CartService;
import cn.waynechu.mmall.util.BigDecimalUtil;
import cn.waynechu.mmall.vo.CartProductVO;
import cn.waynechu.mmall.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author waynechu
 * Created 2018-05-26 11:09
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private FtpServerProperties ftpServerProperties;

    @Override
    public Result<CartVO> list(Long userId) {
        CartVO cartVO = this.getCartVOList(userId);
        return Result.createBySuccess(cartVO);
    }

    @Override
    public Result<CartVO> add(Long userId, Long productId, Integer count) {
        Product selectProduct = productMapper.selectByPrimaryKey(productId);
        if (selectProduct == null) {
            return Result.createByErrorMessage("不存在该商品");
        }
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart == null) {
            // 新加入购物车
            Cart insertCart = new Cart();
            insertCart.setUserId(userId);
            insertCart.setProductId(productId);
            insertCart.setQuantity(count);
            insertCart.setChecked(Const.CartStatus.CHECKED);
            cartMapper.insert(insertCart);
        } else {
            // 已经在购物车，更新数量
            count += cart.getQuantity();
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return this.list(userId);
    }

    @Override
    public Result<CartVO> update(Long userId, Long productId, Integer count) {
        Cart selectCart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (selectCart == null) {
            return Result.createByErrorMessage("购物车中没有该商品");
        }
        selectCart.setQuantity(count);
        cartMapper.updateByPrimaryKey(selectCart);
        return this.list(userId);
    }

    @Override
    public Result<CartVO> delete(Long userId, String productIds) {
        String[] ids = productIds.split(",");
        ArrayList<Long> deleteProductIds = new ArrayList<>();
        for (String id : ids) {
            try {
                Long productId = Long.valueOf(id);
                deleteProductIds.add(productId);
            } catch (NumberFormatException e) {
                return Result.createByErrorMessage("参数格式不正确，请输入数字并按逗号分割");
            }
        }
        cartMapper.deleteByUserIdAndProductIds(userId, deleteProductIds);
        return this.list(userId);
    }

    @Override
    public Result<CartVO> selectOrUnSelect(Long userId, Long productId, int checked) {
        if (productId == null) {
            // 未指定商品，则操作购物车中全部商品
            cartMapper.updateAllCheckedStatusByUserId(userId, checked);
        } else {
            // 更新购物车中指定商品
            cartMapper.updateCheckedStatusByUserIdAndProductId(userId, productId, checked);
        }
        return this.list(userId);
    }

    @Override
    public Result<Integer> getCartProductCount(Long userId) {
        int count = cartMapper.countCartProductByUserId(userId);
        return Result.createBySuccess(count);
    }

    /**
     * 根据用户id获取购物车详情
     *
     * @param userId 用户id
     * @return 购物车详情
     */
    private CartVO getCartVOList(Long userId) {
        CartVO cartVO = new CartVO();
        // 初始化购物车总价格为0
        BigDecimal cartTotalPrice = new BigDecimal("0");

        List<Cart> cartList = cartMapper.selectByUserId(userId);
        // 购物车为空，返回时设置购物车总价为0
        if (cartList.isEmpty()) {
            cartVO.setCartTotalPrice(cartTotalPrice);
            return cartVO;
        }

        List<Long> productIds = cartList.stream().map(Cart::getProductId).collect(Collectors.toList());
        Map<Long, Product> productMap = productMapper.mapProductByProductIds(productIds);

        List<CartProductVO> cartProductVOList = new ArrayList<>();
        CartProductVO cartProductVO;
        for (Cart cart : cartList) {
            cartProductVO = new CartProductVO();
            cartProductVO.setId(cart.getId());
            cartProductVO.setUserId(cart.getUserId());
            cartProductVO.setProductId(cart.getProductId());

            Product product = productMap.get(cart.getProductId());
            // 判断库存
            int buyLimitCount;
            if (product.getStock() >= cart.getQuantity()) {
                // 库存充足时为购物车的购买数量不变
                buyLimitCount = cart.getQuantity();
                cartProductVO.setLimitQuantity(Const.CartStatus.LIMIT_NUM_SUCCESS);
            } else {
                // 库存不足时设置为剩余库存量
                buyLimitCount = product.getStock();
                cartProductVO.setLimitQuantity(Const.CartStatus.LIMIT_NUM_FAIL);
                // 更新购物车中的有效库存
                Cart cartForUpdateQuantity = new Cart();
                cartForUpdateQuantity.setId(cart.getId());
                cartForUpdateQuantity.setQuantity(buyLimitCount);
                cartMapper.updateByPrimaryKeySelective(cartForUpdateQuantity);
            }
            cartProductVO.setQuantity(buyLimitCount);

            cartProductVO.setProductName(product.getName());
            cartProductVO.setProductSubtitle(product.getSubtitle());
            cartProductVO.setProductMainImage(product.getMainImage());
            cartProductVO.setProductPrice(product.getPrice());
            cartProductVO.setProductStatus(product.getStatus());
            // 设置单个商品的总价格
            cartProductVO.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartProductVO.getQuantity()));
            cartProductVO.setProductStock(product.getStock());
            cartProductVO.setProductChecked(cart.getChecked());

            if (cart.getChecked().equals(Const.CartStatus.CHECKED)) {
                // 如果当前购物车中当前商品已选择，就加入总价格计算中
                cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVO.getProductTotalPrice().doubleValue());
            }
            cartProductVOList.add(cartProductVO);
        }
        cartVO.setCartProductVOList(cartProductVOList);
        cartVO.setCartTotalPrice(cartTotalPrice);
        cartVO.setAllChecked(this.selectAllCheckedStatus(userId));
        cartVO.setImageHost(ftpServerProperties.getUrlPrefix());
        return cartVO;
    }

    /**
     * 判断购物车中商品是否全部勾选
     *
     * @param userId 用户id
     * @return 勾选：true
     */
    private boolean selectAllCheckedStatus(Long userId) {
        if (userId == null) {
            return false;
        }
        return cartMapper.countUnCheckedByUserId(userId) == 0;
    }
}
