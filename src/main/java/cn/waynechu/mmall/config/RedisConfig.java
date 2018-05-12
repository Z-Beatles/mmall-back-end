package cn.waynechu.mmall.config;

import cn.waynechu.mmall.serializer.FastJsonRedisSerializer;
import com.alibaba.fastjson.parser.ParserConfig;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author waynechu
 * Created 2018-05-12 00:25
 */
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    /**
     * 设置@Cacheable 序列化方式
     *
     * @return configuration
     */
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();
        configuration = configuration.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(fastJsonRedisSerializer));
        return configuration;
    }

    /**
     * 重写Redis序列化方式，使用FastJson序列化
     *
     * @param redisConnectionFactory factory
     * @return template
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 指定AutoType白名单
        ParserConfig.getGlobalInstance().addAccept("cn.waynechu.");

        // 设置key的序列化方式为StringRedisSerializer
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        // 设置value的序列化方式FastJsonRedisSerializer
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        redisTemplate.setValueSerializer(fastJsonRedisSerializer);
        redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean("redisTemplateForSession")
    public RedisTemplate<String, Object> redisTemplateForSession(RedisConnectionFactory redisConnectionFactory) {
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(stringSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
