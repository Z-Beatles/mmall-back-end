package cn.waynechu.mmall.mapper;

import cn.waynechu.mmall.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Order selectByUserIdAndOrderNo(@Param("userId") Long userId, @Param("orderNo") Long orderNo);

    Order selectByOrderNo(Long orderNo);

    List<Order> listByUserId(Long userId);

    List<Order> listAll();

    List<Order> listByStatusAndCreateTime(@Param("status") int status, @Param("closeDateTime") LocalDateTime closeDateTime);

    int updateStatusByOrderId(@Param("orderId") Long orderId, @Param("status") int status);
}