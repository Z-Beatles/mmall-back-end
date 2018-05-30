package cn.waynechu.mmall.config;

import com.alibaba.fastjson.parser.ParserConfig;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author waynechu
 * Created 2018-04-12 16:22
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
        FastJsonSerializer<Object> fastJsonRedisSerializer = new FastJsonSerializer<>(Object.class);
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();
        configuration = configuration.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(fastJsonRedisSerializer));
        return configuration;
    }

    /**
     * value为FastJsonRedisSerializer序列化的template
     *
     * @param redisConnectionFactory factory
     * @return template
     */
    @Bean("fastJsonRedisTemplate")
    public RedisTemplate<String, Object> fastJsonRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 指定AutoType序列化白名单
        ParserConfig.getGlobalInstance().addAccept("cn.waynechu.");

        // 设置key的序列化方式为StringRedisSerializer
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        // 设置value的序列化方式为FastJsonRedisSerializer
        FastJsonSerializer<Object> fastJsonRedisSerializer = new FastJsonSerializer<>(Object.class);
        redisTemplate.setValueSerializer(fastJsonRedisSerializer);
        redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
