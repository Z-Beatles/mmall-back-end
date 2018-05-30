package cn.waynechu.mmall.service.impl;

import cn.waynechu.mmall.common.ServerResponse;
import cn.waynechu.mmall.entity.Shipping;
import cn.waynechu.mmall.mapper.ShippingMapper;
import cn.waynechu.mmall.service.ShippingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author waynechu
 * Created 2018-05-26 22:41
 */
@Service
public class ShippingServiceImpl implements ShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ServerResponse<Shipping> select(Long userId, Long shippingId) {
        Shipping shipping = shippingMapper.selectByShippingIdAndUserId(shippingId,userId);
        if (shipping == null) {
            return ServerResponse.createByErrorMessage("无法查询到该地址");
        }
        return ServerResponse.createBySuccess(shipping);
    }

    @Override
    public ServerResponse add(Long userId, Shipping shipping) {
        shipping.setUserId(userId);
        int insertCount = shippingMapper.insert(shipping);
        if (insertCount > 0) {
            Map<String, Long> result = new HashMap<>();
            result.put("shippingId", shipping.getId());
            return ServerResponse.createBySuccess("新增收获地址成功", result);
        }
        return ServerResponse.createByErrorMessage("新增收获地址失败");
    }

    @Override
    public ServerResponse del(Long userId, Long shippingId) {
        int deleteCount = shippingMapper.deleteByShippingIdAndUserId(shippingId, userId);
        if (deleteCount > 0) {
            return ServerResponse.createBySuccessMessage("删除收获地址成功");
        }
        return ServerResponse.createByErrorMessage("删除收获地址失败");
    }

    @Override
    public ServerResponse update(Long userId, Shipping shipping) {
        shipping.setUserId(userId);
        int updateCount = shippingMapper.updateByShipping(shipping);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMessage("更新收获地址成功");
        }
        return ServerResponse.createByErrorMessage("更新收获地址失败");
    }

    @Override
    public ServerResponse<PageInfo> list(Long userId, int pageNum, int pageSize, String orderBy) {
        PageHelper.startPage(pageNum, pageSize, orderBy);
        List<Shipping> shippings = shippingMapper.selectByUserId(userId);
        PageInfo<Shipping> pageInfo = new PageInfo<>(shippings);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
