package cn.waynechu.mmall.mapper;

import cn.waynechu.mmall.entity.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    List<OrderItem> listByOrderNoAndUserId(@Param("orderNo") Long orderNo, @Param("userId") Integer userId);

    void batchInsert(List<OrderItem> orderItemList);

    List<OrderItem> listByOrderNo(Long orderNo);
}