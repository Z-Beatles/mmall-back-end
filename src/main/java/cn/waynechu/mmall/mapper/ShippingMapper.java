package cn.waynechu.mmall.mapper;

import cn.waynechu.mmall.entity.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    Shipping selectByShippingIdAndUserId(@Param("shippingId") Long shippingId, @Param("userId") Long userId);

    int deleteByShippingIdAndUserId(@Param("shippingId") Long shippingId, @Param("userId") Long userId);

    List<Shipping> selectByUserId(Long userId);
}