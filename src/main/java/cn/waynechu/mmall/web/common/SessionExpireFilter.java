package cn.waynechu.mmall.web.common;

import cn.waynechu.mmall.util.CookieUtil;
import cn.waynechu.mmall.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 重置session有效期的filter
 * 使用@WebFilter标记filter，并在启动类上添加@ServletComponentScan使扫描到该Filter
 *
 * @author waynechu
 * Created 2018-05-31 23:23
 */
@Order(1)
@WebFilter(filterName = "sessionExpireFilter", urlPatterns = "/*")
@Component
public class SessionExpireFilter implements Filter {

    @Autowired
    private RedisTemplate<String, Object> fastJsonRedisTemplate;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Do nothing here.
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (loginToken != null) {
            UserInfoVO currentUser = (UserInfoVO) fastJsonRedisTemplate.opsForValue().get(loginToken);
            if (currentUser != null) {
                // 如果当前用户已经登录，更新session有效期

                fastJsonRedisTemplate.expire(loginToken, 30L, TimeUnit.MINUTES);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        // Do nothing here.
    }
}
