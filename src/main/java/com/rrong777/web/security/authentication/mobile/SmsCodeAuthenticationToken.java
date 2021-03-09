package com.rrong777.web.security.authentication.mobile;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;

/**
 * 封装登录信息
 * 身份认证前 存的是手机号
 * 认证之后，存的就是身份信息
 * 参考UsernamePasswordAuthenticationToken去写
 *
 * 1. 继承了AbstractAuthenticationToken
 */
public class SmsCodeAuthenticationToken extends AbstractAuthenticationToken {
    // 抽象方法需要实现
    @Override
    public Object getCredentials() {
        return null;
    }

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
    // 这个属性放认证信息，认证之前放手机号，认证之后放登录成功的用户
    private final Object principal;
    // UsernamePasswordAuthenticationToken 中 这个属性放的是密码 但是在这里是没用的，因为我们是在
    // SmsAuthenticationFilter之前加了一个过滤器验证的短信验证码
//    private Object credentials;

    // 这里是没登陆的时候调用的构造器，放手机就可以了
    public SmsCodeAuthenticationToken(String mobile) {
        super((Collection)null);
        this.principal = mobile;
        this.setAuthenticated(false);
    }


    // 登录成功principal放的是认证信息
    public SmsCodeAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }

}
