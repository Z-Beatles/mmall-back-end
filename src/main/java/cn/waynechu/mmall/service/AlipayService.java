package cn.waynechu.mmall.service;

import cn.waynechu.mmall.common.Result;

import java.util.Map;

/**
 * @author waynechu
 * Created 2018-06-05 12:21
 */
public interface AlipayService {

    Result queryPayStatus(Long orderNo);

    Result pay(Long orderNo, Long userId, String path);

    Result checkPayParams(Map<String, String> params);
}
