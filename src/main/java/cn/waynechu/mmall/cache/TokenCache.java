package cn.waynechu.mmall.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author waynechu
 * Created 2018-05-21 23:01
 */
@Slf4j
public class TokenCache {

    public static final String TOKEN_PREFIX = "token_";

    private static Cache<String, String> localCache = Caffeine.newBuilder().initialCapacity(1000).maximumSize(10000)
            .expireAfterAccess(12, TimeUnit.HOURS)
            .build();

    public static void setKey(String key, String value) {
        localCache.put(key, value);
    }

    public static String getKey(String key) {
        return localCache.getIfPresent(key);
    }

    public static void deleteKey(String key) {
        localCache.invalidate(key);
    }
}
