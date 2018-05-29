package cn.waynechu.mmall.service;

import cn.waynechu.mmall.common.ServerResponse;
import cn.waynechu.mmall.vo.OrderVO;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @author waynechu
 * Created 2018-05-28 13:23
 */
public interface OrderService {
    ServerResponse pay(Long orderNo, Integer userId, String path);

    ServerResponse aliCallback(Map<String, String> params);

    ServerResponse queryOrderPayStatus(Long orderNo, Integer userId);

    /**
     * 创建订单
     *
     * @param userId     用户id
     * @param shippingId 收货地址id
     * @return 订单详情
     */
    ServerResponse createOrder(Integer userId, Integer shippingId);

    /**
     * 取消订单
     *
     * @param userId  用户id
     * @param orderNo 订单id
     * @return string
     */
    ServerResponse<String> cancel(Integer userId, Long orderNo);

    ServerResponse getOrderCartProduct(Integer userId);

    /**
     * 获取订单详情
     *
     * @param userId  用户id
     * @param orderNo 订单id
     * @return 订单详情
     */
    ServerResponse<OrderVO> getOrderDetail(Integer userId, Long orderNo);

    /**
     * 获取指定用户订单列表
     *
     * @param userId   用户id
     * @param pageNum  页数
     * @param pageSize 页大小
     * @return 订单列表
     */
    ServerResponse getOrderList(Integer userId, int pageNum, int pageSize);

    ServerResponse<PageInfo> manageList(int pageNum, int pageSize, String orderBy);

    ServerResponse<OrderVO> manageDetail(Long orderNo);

    ServerResponse<PageInfo> manageSearch(Long orderNo, int pageNum, int pageSize);

    ServerResponse<String> manageSendGoods(Long orderNo);
}
