package cn.waynechu.mmall;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * @author waynechu
 * Created 2018-05-26 10:51
 */
public class BigDecimalTest {

    @Test
    public void test1() {
        System.out.println(0.05 + 0.01);
        System.out.println(1.0 - 0.42);
        System.out.println(4.015 * 100);
        System.out.println(123.3 / 100);
    }

    @Test
    public void test2() {
        BigDecimal num1 = new BigDecimal(0.05);
        BigDecimal num2 = new BigDecimal(0.01);
        System.out.println(num1.add(num2));
    }

    @Test
    public void test3() {
        BigDecimal num1 = new BigDecimal("0.05");
        BigDecimal num2 = new BigDecimal("0.01");
        System.out.println(num1.add(num2));
    }
}

