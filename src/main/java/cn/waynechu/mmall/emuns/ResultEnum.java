package cn.waynechu.mmall.emuns;

/**
 * @author waynechu
 * Created 2018-05-12 00:20
 */
public enum ResultEnum {

    /**
     * 系统异常
     */
    SYSTEM_ERROR(-1, "系统异常"),
    /**
     * 请求成功
     **/
    SUCCESS(0, "success"),
    /**
     * 请求失败
     **/
    FAILED(1, "failed"),
    ACCOUNT_NOT_EXIST_ERROR(1000, "账号不存在"),
    WRONG_PASSWORD_ERROR(1003, "密码错误"),
    LOGIN_FAILED_ERROR(1004, "登陆失败"),
    REPEAT_LOGIN_ERROR(1005, "您已登录，勿重复登录"),
    NOT_LOGIN_ERROR(1006, "抱歉，您尚未登录"),
    LOGIN_SUCCEED_INFO(1007, "登陆成功"),
    LOGOUT_SUCCEED_INFO(1008, "退出成功"),
    REGISTER_SUCCESS_INFO(1009, "注册成功"),
    ACCOUNT_EXIST_ERROR(1010, "该账号已被注册"),
    WITHOUT_PERMISSION_ERROR(1011, "没有权限查看他人信息"),
    REGISTER_FAILED_ERROR(1012, "注册失败"),
    MISSING_SERVLET_REQUEST_PARAMETER_ERROR(1013, "缺少请求参数"),
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
