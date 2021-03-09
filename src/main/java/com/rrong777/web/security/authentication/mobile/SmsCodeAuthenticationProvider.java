package com.rrong777.web.security.authentication.mobile;


import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 身份认证提供者
 */
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    // 进行身份认证的逻辑封装在这个方法
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 这个主要就是用UserdetailsSerivce获取用户信息
        SmsCodeAuthenticationToken smsCodeAuthenticationToken = (SmsCodeAuthenticationToken) authentication;
        // 从未认证的authentication对象（token）里面取出手机号，再根据这个手机号去数据库里面取用户
        UserDetails user = userDetailsService.loadUserByUsername((String) smsCodeAuthenticationToken.getPrincipal());
        if(user == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息！");
        }

        // 用这个构造器，创建出来的是已认证的authentication对象，我们会再SmsCodeAuthenticationFilter之前就验证短信验证码
        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(user, user.getAuthorities());
        // 把未认证的details设置到已认证里面去
        authenticationResult.setDetails(authentication.getDetails());
        return authenticationResult;
    }

    // AuthenticationManager里面挑一个provider去处理就是使用这个support判断传进来的authentication对象 当前provider是否能处理
    @Override
    public boolean supports(Class<?> aClass) {
        // 判断传进来的class是不是SmsCodeAuthenticationToken 这种类型的
        return SmsCodeAuthenticationToken.class.isAssignableFrom(aClass);
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
