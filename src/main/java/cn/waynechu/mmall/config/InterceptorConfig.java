package cn.waynechu.mmall.config;

import cn.waynechu.mmall.web.common.AuthorityInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author waynechu
 * Created 2018-06-01 22:23
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthorityInterceptor())
                .addPathPatterns("/v1/**")
                // 门户
                .excludePathPatterns("/v1/user/register.do")
                .excludePathPatterns("/v1/user/login.do")
                .excludePathPatterns("/v1/user/logout.do")
                .excludePathPatterns("/v1/user/check_valid.do")
                .excludePathPatterns("/v1/user/get_user_info.do")
                .excludePathPatterns("/v1/user/forget_get_question.do")
                .excludePathPatterns("/v1/user/forget_check_answer.do")
                .excludePathPatterns("/v1/user/forget_reset_password.do")
                .excludePathPatterns("/v1/products/**")
                .excludePathPatterns("/v1/order/alipay_callback.do")
                // 后台
                .excludePathPatterns("/v1/manager/user/login.do")
                .excludePathPatterns("/v1/manager/product/richtext_img_upload.do");
    }
}
