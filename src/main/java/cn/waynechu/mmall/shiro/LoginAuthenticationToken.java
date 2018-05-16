package cn.waynechu.mmall.shiro;

import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

/**
 * @author waynechu
 * Created 2017-10-23 14:06
 */
public class LoginAuthenticationToken implements HostAuthenticationToken, RememberMeAuthenticationToken {

    private String loginType;
    private String account;
    private String password;
    private boolean rememberMe;
    private String host;

    public LoginAuthenticationToken(String loginType, String account, String password, boolean rememberMe, String host) {
        this.loginType = loginType;
        this.account = account;
        this.password = password;
        this.rememberMe = rememberMe;
        this.host = host;
    }

    public String getLoginType() {
        return loginType;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public Object getPrincipal() {
        return "[" + loginType + "," + account + "]";
    }

    @Override
    public Object getCredentials() {
        return password;
    }

    @Override
    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    @Override
    public String getHost() {
        return host;
    }
}
