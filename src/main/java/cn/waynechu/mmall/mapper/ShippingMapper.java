package cn.waynechu.mmall.mapper;

import cn.waynechu.mmall.entity.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    Shipping selectByShippingIdAndUserId(@Param("shippingId") Integer shippingId, @Param("userId") Integer userId);

    int deleteByShippingIdAndUserId(@Param("shippingId") Integer shippingId, @Param("userId") Integer userId);

    int updateByShipping(Shipping shipping);

    List<Shipping> selectByUserId(Integer userId);
}