package cn.waynechu.mmall.task;

import cn.waynechu.mmall.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${task.close-order.time}")
    private int closeOrderDelay;

    /**
     * 每分钟执行：关闭在closeOrderDelay时间之前未支付的订单
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTask() {
        log.info("[定时关单] ---------- start ------------");
        orderService.closeOrder(closeOrderDelay);
        log.info("[定时关单] ----------- end -------------");
    }
}
