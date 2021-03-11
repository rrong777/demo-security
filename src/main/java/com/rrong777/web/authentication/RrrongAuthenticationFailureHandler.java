package com.rrong777.web.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rrong777.web.properties.LoginType;
import com.rrong777.web.properties.SecurityProperties;
import com.rrong777.web.support.SimpleResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component("rrongAuthenticationFailureHandler")
//public class RrrongAuthenticationFailureHandler implements AuthenticationFailureHandler {
// 与登录成功处理器一样的处理，继承Security默认的登录失败处理
public class RrrongAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private ObjectMapper objectMapper;
    /**
     * 认证失败处理器
     * @param request
     * @param response
     * @param exception 认证过程中抛出的异常（没有用户，密码错误等）
     *                  登录失败是没有拿到authentication对象的，都没登录成功，哪里来的认证结果，
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        logger.info("登录失败");
        if(LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json;charset=UTF-8");
            // 异常转换未json输出太长了，只要msg转回就行了
            response.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse(exception.getMessage())));
        } else {
            // Spring默认的处理方式，跳转到一个错误页面去
            super.onAuthenticationFailure(request,response, exception);
        }

    }
}
