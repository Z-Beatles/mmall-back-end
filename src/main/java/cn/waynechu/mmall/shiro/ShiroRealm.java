package cn.waynechu.mmall.shiro;

import cn.waynechu.mmall.entity.User;
import cn.waynechu.mmall.mapper.UserMapper;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author waynechu
 * Created 2017-10-23 14:04
 */
@Component
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserMapper userMapper;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        LoginAuthenticationToken myToken = (LoginAuthenticationToken) token;
        User user = userMapper.getByUsername(myToken.getUsername());

        if (user == null) {
            throw new UnknownAccountException("账号[" + myToken.getLoginType() + "," + myToken.getUsername() + "]不存在");
        }
        return new SimpleAuthenticationInfo(user, user.getPasswordHash(), ByteSource.Util.bytes(user.getPasswordSalt()), getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @PostConstruct
    public void initCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher("SHA-1");
        matcher.setHashIterations(1024);
        this.setCredentialsMatcher(matcher);
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof LoginAuthenticationToken || super.supports(token);
    }
}
