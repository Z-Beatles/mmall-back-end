package cn.waynechu.mmall.service;

import cn.waynechu.mmall.common.ServerResponse;
import cn.waynechu.mmall.entity.Shipping;
import com.github.pagehelper.PageInfo;

/**
 * @author waynechu
 * Created 2018-05-26 22:41
 */
public interface ShippingService {

    ServerResponse<Shipping> select(Long userId, Long shippingId);

    ServerResponse add(Long userId, Shipping shipping);

    ServerResponse del(Long userId, Long shippingId);

    ServerResponse update(Long userId, Shipping shipping);

    ServerResponse<PageInfo> list(Long userId, int pageNum, int pageSize, String orderBy);
}
