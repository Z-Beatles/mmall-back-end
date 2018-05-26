package cn.waynechu.mmall.util;

import java.math.BigDecimal;

/**
 * @author waynechu
 * Created 2018-05-26 13:36
 */
public class BigDecimalUtil {

    private BigDecimalUtil() {
    }

    /**
     * 加法
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 和
     */
    public static BigDecimal add(double v1, double v2) {
        BigDecimal bigDecimal1 = new BigDecimal(Double.toString(v1));
        BigDecimal bigDecimal2 = new BigDecimal(Double.toString(v2));
        return bigDecimal1.add(bigDecimal2);
    }

    /**
     * 减法
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 差
     */
    public static BigDecimal sub(double v1, double v2) {
        BigDecimal bigDecimal1 = new BigDecimal(Double.toString(v1));
        BigDecimal bigDecimal2 = new BigDecimal(Double.toString(v2));
        return bigDecimal1.subtract(bigDecimal2);
    }

    /**
     * 乘法
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 积
     */
    public static BigDecimal mul(double v1, double v2) {
        BigDecimal bigDecimal1 = new BigDecimal(Double.toString(v1));
        BigDecimal bigDecimal2 = new BigDecimal(Double.toString(v2));
        return bigDecimal1.multiply(bigDecimal2);
    }

    /**
     * 除法
     * <p>
     * 四舍五入，保留两位小数
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 商
     */
    public static BigDecimal div(double v1, double v2) {
        BigDecimal bigDecimal1 = new BigDecimal(Double.toString(v1));
        BigDecimal bigDecimal2 = new BigDecimal(Double.toString(v2));
        // 四舍五入，保留两位小数
        return bigDecimal1.divide(bigDecimal2, 2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 除法
     * <p>
     * 四舍五入，保留 scale 位小数
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 精度
     * @return 商
     */
    public static BigDecimal div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal bigDecimal1 = new BigDecimal(Double.toString(v1));
        BigDecimal bigDecimal2 = new BigDecimal(Double.toString(v2));
        return bigDecimal1.divide(bigDecimal2, scale, BigDecimal.ROUND_HALF_UP);
    }

}
