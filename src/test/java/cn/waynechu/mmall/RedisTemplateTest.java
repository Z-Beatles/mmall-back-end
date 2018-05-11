package cn.waynechu.mmall;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author waynechu
 * Created 2018-04-12 12:27
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTemplateTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    @Transactional
    public void testSet() {
        redisTemplate.opsForValue().set("foo", "bar");
        Assert.assertEquals("bar", redisTemplate.opsForValue().get("foo"));
    }
}
