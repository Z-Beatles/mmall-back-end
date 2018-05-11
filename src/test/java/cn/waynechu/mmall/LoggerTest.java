package cn.waynechu.mmall;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author waynechu
 * Created 2018-01-18 21:10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LoggerTest {

    private static final Logger logger = LoggerFactory.getLogger(LoggerTest.class);

    @Test
    public void test() {
        logger.trace("trace ...");
        logger.debug("debug ...");
        logger.info("info ...");
        logger.warn("warn ...");
        logger.error("error ...");
    }
}
