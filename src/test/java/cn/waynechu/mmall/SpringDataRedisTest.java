package cn.waynechu.mmall;

import cn.waynechu.mmall.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

/**
 * @author waynechu
 * Created 2018-05-30 17:53
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringDataRedisTest {

    @Autowired
    private RedisTemplate<String, Object> fastJsonRedisTemplate;

    @Test
    public void test() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 99999; i++) {
            fastJsonRedisTemplate.opsForValue().set("users:admin:waynechu" + i, new User((long) i, "waynechu", "pa4word", "waynechu1996@gmail.com", "15500000000", null, null, 1, LocalDateTime.now(), LocalDateTime.now()));
        }
        long endTime = System.currentTimeMillis();
        System.out.println("fastJsonSerializer time: " + (endTime - startTime));

        long startTime2 = System.currentTimeMillis();
        for (int i = 0; i < 99999; i++) {
            User user = (User) fastJsonRedisTemplate.opsForValue().get("users:admin:waynechu" + i);
        }
        long endTime2 = System.currentTimeMillis();
        System.out.println("fastJsonDeSerializer time: " + (endTime2 - startTime2));
    }
}
