package cn.waynechu.mmall.common;

/**
 * @author waynechu
 * Created 2018-05-21 20:13
 */
public class Const {

    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";
    public static final String USERNAME = "username";
    public static final String RESET_PASSWORD_TOKEN_PREFIX = "token:reset_password:";

    public interface Role {
        /**
         * 普通用户
         **/
        int ROLE_CUSTOMER = 0;
        /**
         * 管理员
         **/
        int ROLE_ADMIN = 1;
    }

    public interface CategoryStatus {
        /**
         * 正常使用
         **/
        int USAGE = 0;
        /**
         * 未使用
         **/
        int UNUSAGE = 1;
    }

    public interface CartStatus {
        /**
         * 未勾选
         **/
        int UN_CHECKED = 0;
        /**
         * 已勾选
         **/
        int CHECKED = 1;

        /**
         * 购买数量在库存上限以内
         **/
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
        /**
         * 购买数量超出库存上限
         **/
        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
    }

    public enum ProductStatusEnum {
        /**
         * 下架状态
         **/
        NOT_SALE(0, "下架"),
        /**
         * 在售状态
         **/
        ON_SALE(1, "在售"),
        /**
         * 伪删除
         **/
        DELETE(2, "删除");

        private int code;
        private String value;

        ProductStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        public static ProductStatusEnum codeOf(int code) {
            for (ProductStatusEnum productStatusEnum : values()) {
                if (productStatusEnum.getCode() == code) {
                    return productStatusEnum;
                }
            }
            throw new RuntimeException("未找到该枚举类型");
        }
    }

    public enum OrderStatusEnum {
        CANCELED(0, "已取消"),
        NO_PAY(10, "未支付"),
        PAID(20, "已付款"),
        SHIPPED(40, "已发货"),
        ORDER_SUCCESS(50, "订单完成"),
        ORDER_CLOSE(60, "订单关闭");

        private int code;
        private String value;

        OrderStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        public static OrderStatusEnum codeOf(int code) {
            for (OrderStatusEnum orderStatusEnum : values()) {
                if (orderStatusEnum.getCode() == code) {
                    return orderStatusEnum;
                }
            }
            throw new RuntimeException("未找到该枚举类型");
        }
    }

    public interface AlipayCallback {
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";

        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }

    public enum PayPlatformEnum {
        ALIPAY(1, "支付宝");

        private int code;
        private String value;

        PayPlatformEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }

    public enum PaymentTypeEnum {
        ONLINE_PAY(1, "在线支付");

        private int code;
        private String value;

        PaymentTypeEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        public static PaymentTypeEnum codeOf(int code) {
            for (PaymentTypeEnum paymentTypeEnum : values()) {
                if (paymentTypeEnum.getCode() == code) {
                    return paymentTypeEnum;
                }
            }
            throw new RuntimeException("未找到该枚举类型");
        }
    }
}
