package cn.waynechu.mmall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * @author waynechu
 * Created 2018-06-01 16:00
 */
@Configuration
public class SessionConfig {

    /**
     * Custom Serializer for Spring Session in Redis
     * (Using FastJsonSerializer and default is JdkSerializationRedisSerializer)
     *
     * @return serializer
     */
    @Bean("springSessionDefaultRedisSerializer")
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new FastJsonSerializer<>(Object.class);
    }

    /**
     * Set defaultCookieSerializer bean
     *
     * @return defaultCookieSerializer
     */
    @Bean("defaultCookieSerializer")
    public DefaultCookieSerializer defaultCookieSerializer() {
        DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
        defaultCookieSerializer.setCookieName("sid");
        defaultCookieSerializer.setDomainName("waynechu.com");
        defaultCookieSerializer.setCookiePath("/");
        defaultCookieSerializer.setCookieMaxAge(60 * 60 * 24 * 7);
        defaultCookieSerializer.setUseHttpOnlyCookie(true);
        return defaultCookieSerializer;
    }
}
