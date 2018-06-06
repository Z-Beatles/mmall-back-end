package cn.waynechu.mmall.util;

import java.util.regex.Pattern;

/**
 * @author waynechu
 * Created 2017-10-25 11:43
 */
public class RegexUtil {

    private RegexUtil() {
    }

    /** 用户名：需为4到16位的字母、数字、下划线或减号 **/
    private static Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{4,16}$");

    /** 密码：以字母开头，长度在6~18的非空字符 **/
    private static Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z]\\S{5,17}$");

    /** 邮箱 */
    private static Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");

    /** 电话 */
    private static Pattern MOBILE_PATTERN =
            Pattern.compile("^1(3[0-9]|4[57]|5[0-35-9]|7[01678]|8[0-9])[0-9]{8}$");

    public static boolean matchUsername(String str) {
        return USERNAME_PATTERN.matcher(str).matches();
    }

    public static boolean matchPassword(String str) {
        return PASSWORD_PATTERN.matcher(str).matches();
    }

    public static boolean matchEmail(String str) {
        return EMAIL_PATTERN.matcher(str).matches();
    }

    public static boolean matchMobile(String str) {
        return MOBILE_PATTERN.matcher(str).matches();
    }

    public static boolean match(String pattern, String str) {
        return Pattern.compile(pattern).matcher(str).matches();
    }
}
