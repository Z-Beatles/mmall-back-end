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

    public enum ProductStatusEnum{
        /** 下架状态 **/
        NOT_SALE(0, "下架"),
        /** 在售状态 **/
        ON_SALE(1, "在售"),
        /** 伪删除 **/
        DELETE(2, "删除");

        private String value;
        private int code;

        ProductStatusEnum(int code,String value){
            this.code = code;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }
}
