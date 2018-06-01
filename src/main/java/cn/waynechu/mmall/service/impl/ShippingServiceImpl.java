package cn.waynechu.mmall.service.impl;

import cn.waynechu.mmall.common.Result;
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
    public Result<Shipping> select(Long userId, Long shippingId) {
        Shipping shipping = shippingMapper.selectByShippingIdAndUserId(shippingId,userId);
        if (shipping == null) {
            return Result.createByErrorMessage("无法查询到该地址");
        }
        return Result.createBySuccess(shipping);
    }

    @Override
    public Result add(Long userId, Shipping shipping) {
        shipping.setUserId(userId);
        int insertCount = shippingMapper.insert(shipping);
        if (insertCount > 0) {
            Map<String, Long> result = new HashMap<>();
            result.put("shippingId", shipping.getId());
            return Result.createBySuccess("新增收获地址成功", result);
        }
        return Result.createByErrorMessage("新增收获地址失败");
    }

    @Override
    public Result del(Long userId, Long shippingId) {
        int deleteCount = shippingMapper.deleteByShippingIdAndUserId(shippingId, userId);
        if (deleteCount > 0) {
            return Result.createBySuccessMessage("删除收获地址成功");
        }
        return Result.createByErrorMessage("删除收获地址失败");
    }

    @Override
    public Result update(Long userId, Shipping shipping) {
        shipping.setUserId(userId);
        int updateCount = shippingMapper.updateByShipping(shipping);
        if (updateCount > 0) {
            return Result.createBySuccessMessage("更新收获地址成功");
        }
        return Result.createByErrorMessage("更新收获地址失败");
    }

    @Override
    public Result<PageInfo> list(Long userId, int pageNum, int pageSize, String orderBy) {
        PageHelper.startPage(pageNum, pageSize, orderBy);
        List<Shipping> shippings = shippingMapper.selectByUserId(userId);
        PageInfo<Shipping> pageInfo = new PageInfo<>(shippings);
        return Result.createBySuccess(pageInfo);
    }
}
