package cn.waynechu.mmall.service;

import cn.waynechu.mmall.common.ServerResponse;
import cn.waynechu.mmall.entity.Shipping;
import com.github.pagehelper.PageInfo;

/**
 * @author waynechu
 * Created 2018-05-26 22:41
 */
public interface ShippingService {

    ServerResponse<Shipping> select(Integer userId, Integer shippingId);

    ServerResponse add(Integer userId, Shipping shipping);

    ServerResponse del(Integer userId, Integer shippingId);

    ServerResponse update(Integer id, Shipping shipping);

    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize, String orderBy);
}
