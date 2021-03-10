package com.rrong777.web.security.authentication.mobile;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 需要把这个filter和provider配置到security链条上 （provider要被manager调用）  token是用来封装数据的，有被调用，不用配置到链条上
 */
public class SmsCodeAuthenticationFilter extends
        AbstractAuthenticationProcessingFilter {
    // 这里这个参数是说：在请求中你携带手机号的参数是什么
    public static final String RONG_FORM_MOBILE_KEY = "mobile";
    private String mobileParameter = RONG_FORM_MOBILE_KEY;

    // 是否只处理post请求
    private boolean postOnly = true;


    public SmsCodeAuthenticationFilter() {
        // 请求处理的url 和处理的请求的方式
        super(new AntPathRequestMatcher("/authentication/mobile", "POST"));
    }


    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        String mobile = obtainMobile(request);

        if (mobile == null) {
            mobile = "";
        }
        mobile = mobile.trim();

        SmsCodeAuthenticationToken authRequest = new SmsCodeAuthenticationToken(mobile);

        // 把请求的信息set到token里面去
        setDetails(request, authRequest);
        // 调用authenticationManager
        return this.getAuthenticationManager().authenticate(authRequest);
    }


    /**
     * 从请求中获取手机号的方法
     * @param request
     * @return
     */
    protected String obtainMobile(HttpServletRequest request) {
        return request.getParameter(mobileParameter);
    }

    /**
     * 把请求的ip和sessionId等信息放到认证请求里面去
     * @param request
     * @param authRequest
     */
    protected void setDetails(HttpServletRequest request,
                              SmsCodeAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }


    public void setMobileParameter(String mobileParameter) {
        Assert.hasText(mobileParameter, "Username parameter must not be empty or null");
        this.mobileParameter = mobileParameter;
    }



    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getMobileParameter() {
        return mobileParameter;
    }

}
