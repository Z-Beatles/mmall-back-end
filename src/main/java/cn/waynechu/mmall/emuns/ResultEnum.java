package cn.waynechu.mmall.emuns;

/**
 * @author waynechu
 * Created 2018-05-12 00:20
 */
public enum ResultEnum {

    /** 系统异常 */
    SYSTEM_ERROR(-1, "系统异常"),
    /** 请求成功 **/
    SUCCESS(0, "success"),
    /** 请求失败 **/
    FAILED(1, "failed"),
    NOT_LOGIN_ERROR(1000, "尚未登录"),
    ;

    private Integer code;

    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
