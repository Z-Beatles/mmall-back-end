package cn.waynechu.mmall.service.impl;

import cn.waynechu.mmall.common.Const;
import cn.waynechu.mmall.common.Result;
import cn.waynechu.mmall.entity.*;
import cn.waynechu.mmall.mapper.*;
import cn.waynechu.mmall.properties.FtpServerProperties;
import cn.waynechu.mmall.service.OrderService;
import cn.waynechu.mmall.util.BigDecimalUtil;
import cn.waynechu.mmall.util.DateTimeUtil;
import cn.waynechu.mmall.vo.OrderItemVO;
import cn.waynechu.mmall.vo.OrderProductVO;
import cn.waynechu.mmall.vo.OrderVO;
import cn.waynechu.mmall.vo.ShippingVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author waynechu
 * Created 2018-05-28 13:23
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ShippingMapper shippingMapper;

    @Autowired
    private FtpServerProperties ftpServerProperties;

    @Override
    public Result queryOrderPayStatus(Long orderNo, Long userId) {
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            return Result.createByErrorMessage("用户没有该订单");
        }
        if (order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()) {
            return Result.createBySuccess();
        }
        return Result.createByError();
    }

    @Override
    public Result createOrder(Long userId, Long shippingId) {
        // 从购物车中获取已勾选的购物列表
        List<Cart> cartList = cartMapper.listCheckedCartByUserId(userId);

        // 根据购物车勾选的商品列表生成订单详情列表
        Result<List<OrderItem>> result = this.getCartOrderItem(userId, cartList);
        if (!result.isSuccess()) {
            return result;
        }
        // 计算这个订单总价(校验库存)
        List<OrderItem> orderItemList = result.getData();
        BigDecimal payment = this.getOrderTotalPrice(orderItemList);

        // 生成订单信息
        Order order = this.assembleOrder(userId, shippingId, payment);
        if (order == null) {
            return Result.createByErrorMessage("生成订单错误");
        }
        if (orderItemList.isEmpty()) {
            Result.createByErrorMessage("购物车为空");
        }
        // 将订单的订单号传入订单详情中
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderNo(order.getOrderNo());
        }
        // 将订单详情item批量插入
        orderItemMapper.batchInsert(orderItemList);

        // 生成订单成功，进行减库存操作
        this.reduceProductStock(orderItemList);

        // 清空购物车(已勾选)
        this.cleanCat(cartList);

        OrderVO orderVO = this.assembleOrderVO(order, orderItemList);
        return Result.createBySuccess(orderVO);
    }

    @Override
    public Result<String> cancel(Long userId, Long orderNo) {
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            return Result.createByErrorMessage("该用户不存在该订单");
        }
        if (order.getStatus() != Const.OrderStatusEnum.NO_PAY.getCode()) {
            return Result.createByErrorMessage("已付款，无法取消订单");
        }
        Order updateOrder = new Order();
        updateOrder.setId(order.getId());
        updateOrder.setStatus(Const.OrderStatusEnum.CANCELED.getCode());

        int updateCount = orderMapper.updateByPrimaryKeySelective(updateOrder);
        if (updateCount > 0) {
            return Result.createBySuccess();
        }
        return Result.createByError();
    }

    @Override
    public Result getOrderCartProduct(Long userId) {
        OrderProductVO orderProductVO = new OrderProductVO();

        // 获取购物车中勾选的商品列表
        List<Cart> cartList = cartMapper.listCheckedCartByUserId(userId);
        Result<List<OrderItem>> result = this.getCartOrderItem(userId, cartList);
        if (!result.isSuccess()) {
            return result;
        }
        List<OrderItem> orderItemList = result.getData();

        List<OrderItemVO> orderItemVOList = new ArrayList<>();
        BigDecimal payment = new BigDecimal("0");
        for (OrderItem orderItem : orderItemList) {
            payment = BigDecimalUtil.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
            orderItemVOList.add(assembleOrderItemVO(orderItem));
        }

        orderProductVO.setOrderItemVOList(orderItemVOList);
        orderProductVO.setProductTotalPrice(payment);
        orderProductVO.setImageHost(ftpServerProperties.getUrlPrefix());
        return Result.createBySuccess(orderProductVO);
    }

    @Override
    public Result<OrderVO> getOrderDetail(Long userId, Long orderNo) {
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order != null) {
            List<OrderItem> orderItemList = orderItemMapper.listByOrderNoAndUserId(orderNo, userId);
            OrderVO orderVO = assembleOrderVO(order, orderItemList);
            return Result.createBySuccess(orderVO);
        }
        return Result.createByErrorMessage("该用户不存在该订单");
    }

    @Override
    public Result getOrderList(Long userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.listByUserId(userId);
        List<OrderVO> orderVOList = this.assembleOrderVOList(orderList, userId);
        PageInfo pageInfo = new PageInfo<>(orderList);
        pageInfo.setList(orderVOList);
        return Result.createBySuccess(pageInfo);
    }

    @Override
    public Result<PageInfo> manageList(int pageNum, int pageSize, String orderBy) {
        PageHelper.startPage(pageNum, pageSize, orderBy);
        List<Order> orderList = orderMapper.listAll();
        List<OrderVO> orderVOList = this.assembleOrderVOList(orderList, null);
        PageInfo pageInfo = new PageInfo<>(orderList);
        pageInfo.setList(orderVOList);
        return Result.createBySuccess(pageInfo);
    }

    @Override
    public Result<OrderVO> manageDetail(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order != null) {
            List<OrderItem> orderItemList = orderItemMapper.listByOrderNo(orderNo);
            OrderVO orderVO = this.assembleOrderVO(order, orderItemList);
            return Result.createBySuccess(orderVO);
        }
        return Result.createByErrorMessage("订单不存在");
    }

    @Override
    public Result<PageInfo> manageSearch(Long orderNo, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order != null) {
            List<OrderItem> orderItemList = orderItemMapper.listByOrderNo(orderNo);
            OrderVO orderVO = this.assembleOrderVO(order, orderItemList);

            PageInfo pageInfo = new PageInfo<>(Collections.singletonList(order));
            pageInfo.setList(Collections.singletonList(orderVO));
            return Result.createBySuccess(pageInfo);
        }
        return Result.createByErrorMessage("订单不存在");
    }

    @Override
    public Result<String> manageSendGoods(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order != null) {
            if (order.getStatus() == Const.OrderStatusEnum.PAID.getCode()) {
                order.setStatus(Const.OrderStatusEnum.SHIPPED.getCode());
                order.setSendTime(LocalDateTime.now());
                orderMapper.updateByPrimaryKeySelective(order);
                return Result.createBySuccess("发货成功");
            }
            return Result.createByErrorMessage("订单尚未支付");
        }
        return Result.createByErrorMessage("订单不存在");
    }

    @Override
    public void closeOrder(int hour) {
        LocalDateTime closeDateTime = LocalDateTime.now().minusHours(hour);
        List<Order> orderList = orderMapper.listByStatusAndCreateTime(Const.OrderStatusEnum.NO_PAY.getCode(), closeDateTime);
        for (Order order : orderList) {
            List<OrderItem> orderItemList = orderItemMapper.listByOrderNo(order.getOrderNo());
            for (OrderItem orderItem : orderItemList) {
                Integer stock = productMapper.selectStockByProductIdForUpdate(orderItem.getProductId());

                // 如果stock为null表示该商品已经被删除
                if (stock == null) {
                    continue;
                }
                // 将关闭订单的商品数归还库存
                Product product = new Product();
                product.setId(orderItem.getProductId());
                product.setStock(stock + orderItem.getQuantity());
                productMapper.updateByPrimaryKeySelective(product);
            }
            // 更改订单状态为取消状态
            orderMapper.updateStatusByOrderId(order.getId(), Const.OrderStatusEnum.CANCELED.getCode());
            log.info("[关闭订单] 订单号：{}", order.getOrderNo());
        }
    }

    private List<OrderVO> assembleOrderVOList(List<Order> orderList, Long userId) {
        List<OrderVO> orderVOList = new ArrayList<>();
        for (Order order : orderList) {
            List<OrderItem> orderItemList;
            if (userId == null) {
                orderItemList = orderItemMapper.listByOrderNo(order.getOrderNo());
            } else {
                orderItemList = orderItemMapper.listByOrderNoAndUserId(order.getOrderNo(), userId);
            }
            OrderVO orderVO = assembleOrderVO(order, orderItemList);
            orderVOList.add(orderVO);
        }
        return orderVOList;
    }

    private OrderVO assembleOrderVO(Order order, List<OrderItem> orderItemList) {
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderNo(order.getOrderNo());
        orderVO.setPayment(order.getPayment());
        orderVO.setPaymentType(order.getPaymentType());
        orderVO.setPaymentTypeDesc(Const.PaymentTypeEnum.codeOf(order.getPaymentType()).getValue());
        orderVO.setPostage(order.getPostage());
        orderVO.setStatus(order.getStatus());
        orderVO.setStatusDesc(Const.OrderStatusEnum.codeOf(order.getStatus()).getValue());

        orderVO.setImageHost(ftpServerProperties.getUrlPrefix());
        orderVO.setShippingId(order.getShippingId());
        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        if (shipping != null) {
            orderVO.setReceiverName(shipping.getReceiverName());
            orderVO.setShippingVO(this.assembleShippingVO(shipping));
        }

        orderVO.setPaymentTime(DateTimeUtil.toStringFromLocalDateTime(order.getPaymentTime()));
        orderVO.setSendTime(DateTimeUtil.toStringFromLocalDateTime(order.getSendTime()));
        orderVO.setEndTime(DateTimeUtil.toStringFromLocalDateTime(order.getEndTime()));
        orderVO.setCloseTime(DateTimeUtil.toStringFromLocalDateTime(order.getCloseTime()));

        List<OrderItemVO> orderItemVOList = new ArrayList<>();
        for (OrderItem orderItem : orderItemList) {
            OrderItemVO orderItemVO = this.assembleOrderItemVO(orderItem);
            orderItemVOList.add(orderItemVO);
        }
        orderVO.setOrderItemVOList(orderItemVOList);
        return orderVO;
    }

    private OrderItemVO assembleOrderItemVO(OrderItem orderItem) {
        OrderItemVO orderItemVO = new OrderItemVO();
        orderItemVO.setOrderNo(orderItem.getOrderNo());
        orderItemVO.setProductId(orderItem.getProductId());
        orderItemVO.setProductName(orderItem.getProductName());
        orderItemVO.setProductImage(orderItem.getProductImage());
        orderItemVO.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVO.setQuantity(orderItem.getQuantity());
        orderItemVO.setTotalPrice(orderItem.getTotalPrice());
        orderItemVO.setCreateTime(DateTimeUtil.toStringFromLocalDateTime(orderItem.getCreateTime()));
        return orderItemVO;
    }

    private ShippingVO assembleShippingVO(Shipping shipping) {
        ShippingVO shippingVO = new ShippingVO();
        BeanUtils.copyProperties(shipping, shippingVO);
        return shippingVO;
    }

    /**
     * 清空购物车
     *
     * @param cartList 购物车清单
     */
    private void cleanCat(List<Cart> cartList) {
        for (Cart cart : cartList) {
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
    }

    /**
     * 更新库存
     *
     * @param orderItemList 订单详情列表
     */
    private void reduceProductStock(List<OrderItem> orderItemList) {
        for (OrderItem orderItem : orderItemList) {
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            product.setStock(product.getStock() - orderItem.getQuantity());
            productMapper.updateByPrimaryKeySelective(product);
        }
    }

    private Order assembleOrder(Long userId, Long shippingId, BigDecimal payment) {
        Order order = new Order();
        order.setOrderNo(this.generateOrderNo());
        order.setUserId(userId);
        order.setShippingId(shippingId);
        order.setPayment(payment);
        order.setPaymentType(Const.PaymentTypeEnum.ONLINE_PAY.getCode());
        order.setStatus(Const.OrderStatusEnum.NO_PAY.getCode());
        order.setPostage(0);

        int insertCount = orderMapper.insert(order);
        if (insertCount > 0) {
            return order;
        }
        return null;
    }

    /**
     * 生成订单号
     *
     * @return 订单号
     */
    private long generateOrderNo() {
        long currentTime = System.currentTimeMillis();
        return currentTime + new Random().nextInt(100);
    }

    /**
     * 根据订单详情列表计算总价格
     *
     * @param orderItemList 订单详情列表
     * @return 订单总价格
     */
    private BigDecimal getOrderTotalPrice(List<OrderItem> orderItemList) {
        BigDecimal payment = new BigDecimal("0");
        for (OrderItem orderItem : orderItemList) {
            payment = BigDecimalUtil.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
        }
        return payment;
    }

    /**
     * 根据用户购物车勾选的商品列表生成订单详情列表
     *
     * @param userId   用户id
     * @param cartList 购物车勾选的商品列表
     * @return 订单详情列表
     */
    private Result<List<OrderItem>> getCartOrderItem(Long userId, List<Cart> cartList) {
        List<OrderItem> orderItemList = new ArrayList<>();
        if (cartList.isEmpty()) {
            return Result.createByErrorMessage("购物车为空或者未勾选任何商品");
        }

        // 校验购物车的数据，包括产品的状态和数量
        for (Cart cartItem : cartList) {
            OrderItem orderItem = new OrderItem();
            Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
            // 校验产品状态
            if (Const.ProductStatusEnum.ON_SALE.getCode() != product.getStatus()) {
                return Result.createByErrorMessage("产品" + product.getName() + "不在售卖状态");
            }
            // 校验库存数量
            if (cartItem.getQuantity() > product.getStock()) {
                return Result.createByErrorMessage("产品" + product.getName() + "库存不足");
            }

            orderItem.setUserId(userId);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartItem.getQuantity()));
            orderItemList.add(orderItem);
        }
        return Result.createBySuccess(orderItemList);
    }
}
