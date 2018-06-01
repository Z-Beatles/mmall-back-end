package cn.waynechu.mmall.service;

import cn.waynechu.mmall.common.Result;
import cn.waynechu.mmall.vo.OrderVO;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @author waynechu
 * Created 2018-05-28 13:23
 */
public interface OrderService {
    Result pay(Long orderNo, Long userId, String path);

    Result aliCallback(Map<String, String> params);

    Result queryOrderPayStatus(Long orderNo, Long userId);

    /**
     * 创建订单
     *
     * @param userId     用户id
     * @param shippingId 收货地址id
     * @return 订单详情
     */
    Result createOrder(Long userId, Long shippingId);

    /**
     * 取消订单
     *
     * @param userId  用户id
     * @param orderNo 订单id
     * @return string
     */
    Result<String> cancel(Long userId, Long orderNo);

    Result getOrderCartProduct(Long userId);

    /**
     * 获取订单详情
     *
     * @param userId  用户id
     * @param orderNo 订单id
     * @return 订单详情
     */
    Result<OrderVO> getOrderDetail(Long userId, Long orderNo);

    /**
     * 获取指定用户订单列表
     *
     * @param userId   用户id
     * @param pageNum  页数
     * @param pageSize 页大小
     * @return 订单列表
     */
    Result getOrderList(Long userId, int pageNum, int pageSize);

    Result<PageInfo> manageList(int pageNum, int pageSize, String orderBy);

    Result<OrderVO> manageDetail(Long orderNo);

    Result<PageInfo> manageSearch(Long orderNo, int pageNum, int pageSize);

    Result<String> manageSendGoods(Long orderNo);
}
