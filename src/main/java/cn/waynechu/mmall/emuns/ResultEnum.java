package cn.waynechu.mmall.emuns;

/**
 * @author waynechu
 * Created 2018-05-12 00:20
 */
public enum ResultEnum {

    /**
     * 请求成功
     **/
    SUCCESS(0, "SUCCESS"),
    /**
     * 请求失败
     **/
    ERROR(1, "ERROR"),

    MISSING_REQUEST_PARAMETER_ERROR(901, "缺少请求参数"),
    NOT_LOGIN_ERROR(1000, "您尚未登录"),
    REPEAT_LOGIN_ERROR(1001, "您已在线，勿重复登录"),
    ACCOUNT_EXIST_ERROR(1002, "该账号已被注册"),
    ACCOUNT_NOT_EXIST_ERROR(1003, "账号不存在"),
    WRONG_PASSWORD_ERROR(1004, "密码错误"),
    LOGIN_FAILED_ERROR(1005, "登陆失败"),
    REGISTER_FAILED_ERROR(1006, "注册失败"),
    WITHOUT_PERMISSION_ERROR(1007, "没有权限查看他人信息"),
    ;

    private final int code;

    private final String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    ResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
