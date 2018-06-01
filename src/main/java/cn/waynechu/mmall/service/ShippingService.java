package cn.waynechu.mmall.service;

import cn.waynechu.mmall.common.Result;
import cn.waynechu.mmall.entity.Shipping;
import com.github.pagehelper.PageInfo;

/**
 * @author waynechu
 * Created 2018-05-26 22:41
 */
public interface ShippingService {

    Result<Shipping> select(Long userId, Long shippingId);

    Result add(Long userId, Shipping shipping);

    Result del(Long userId, Long shippingId);

    Result update(Long userId, Shipping shipping);

    Result<PageInfo> list(Long userId, int pageNum, int pageSize, String orderBy);
}
