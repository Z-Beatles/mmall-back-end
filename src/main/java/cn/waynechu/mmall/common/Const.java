package cn.waynechu.mmall.common;

/**
 * @author waynechu
 * Created 2018-05-21 20:13
 */
public class Const {

    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public interface Role {
        /** 普通用户 **/
        int ROLE_CUSTOMER = 0;
        /** 管理员 **/
        int ROLE_ADMIN = 1;
    }

    public interface CategoryStatus {
        /** 正常使用 **/
        int USAGE = 0;
        /** 未使用 **/
        int UNUSAGE = 1;
    }
}
