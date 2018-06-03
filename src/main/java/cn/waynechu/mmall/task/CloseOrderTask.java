package cn.waynechu.mmall.task;

import cn.waynechu.mmall.common.Const;
import cn.waynechu.mmall.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author waynechu
 * Created 2018-06-02 19:51
 */
@Slf4j
@Component
public class CloseOrderTask {

    @Autowired
    private OrderService orderService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${task.close-order.time}")
    private int closeOrderDelay;

    @Value("${lock.timeout}")
    private int lockTimeout;

    /**
     * 每分钟执行定时关单：关闭在closeOrderDelay时间之前未支付的订单
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void closeOrderTaskByRedisLock() {
        log.info("[定时关单] ---------- start ------------");
        Boolean getLock = stringRedisTemplate.opsForValue().setIfAbsent(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf((System.currentTimeMillis() + lockTimeout + 1)));
        if (getLock) {
            // 获取锁成功。执行业务逻辑并释放锁
            closeOrderTack();
        } else {
            log.info("not get lock, then judge whether the lock is expire.");
            // 未获取到锁。继续获取锁的过期时间
            String lockValue = stringRedisTemplate.opsForValue().get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            if (lockValue != null && System.currentTimeMillis() > Long.parseLong(lockValue)) {
                // 该锁已超时，设置新值并返回旧值
                log.info("lock is expire, try to get new lock.");
                String getSetValue = stringRedisTemplate.opsForValue().getAndSet(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf((System.currentTimeMillis() + lockTimeout + 1)));
                if (getSetValue == null || (getSetValue != null && lockValue.equals(getSetValue))) {
                    // 已过期的旧值仍然存在。只有最先执行getAndSet()的应用进程才能获取到锁
                    closeOrderTack();
                } else {
                    log.info("not get lock.");
                }
            } else {
                log.info("not get lock.");
            }

        }
        log.info("[定时关单] ----------- end -------------");
    }

    /**
     * 关单业务逻辑
     */
    private void closeOrderTack() {
        log.info("获取 {}, ThreadName:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
        orderService.closeOrder(closeOrderDelay);
        stringRedisTemplate.delete(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        log.info("释放 {}, ThreadName:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
        log.info("=============================");
    }
}
