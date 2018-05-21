package cn.waynechu.mmall.common;

/**
 * @author waynechu
 * Created 2018-05-21 19:44
 */
public enum ResponseCode {

    /** 请求成功 **/
    SUCCESS(0, "SUCCESS"),
    /** 请求失败 **/
    ERROR(1, "ERROR"),
    /** 尚未登录 **/
    NEED_LOGIN(10, "NEED_LOGIN"),
    /** 参数错误 **/
    ILLEGAL_ARGUMENT(2, "ILLEGAL_ARGUMENT");

    private final int code;
    private final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
