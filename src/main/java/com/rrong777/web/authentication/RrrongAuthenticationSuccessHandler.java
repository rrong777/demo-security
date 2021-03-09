package com.rrong777.web.authentication;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rrong777.web.properties.LoginType;
import com.rrong777.web.properties.SecurityProerties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("rrongAuthenticationSuccessHandler") // 声明成一个Spring容器中的bean
//public class RrrongAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
// 此时不再实现这个认证成功处理器接口了，而是继承Security提供的默认的实现
public class RrrongAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SecurityProerties securityProerties;
    @Autowired // Spring启动的时候会自动为我们注入一个ObjectMapper
    private ObjectMapper objectMapper;
    /**
     * 下面这个方法就是登录成功之后会呗调用
     * @param request
     * @param response
     * @param authentication Security的核心接口，封装了认证信息，包括你发起认证请求里面的信息，包括你发起认证请求的ip是多少。
     *                       session是什么
     *                       以及你认证通过以后我们自己写的UserDetails里面封装的用户信息。都是在这个Authentication对象里面的
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.info("登录成功");

        // 自己配置的时候等于这个json。用我们自己的方式 不是的话 就super
        if(LoginType.JSON.equals(securityProerties.getBrowser().getLoginType())) {
            // 然后把Authentication转为JSON返回前台，需要工具类objectMapper
            response.setContentType("application/json;charset=UTF-8");
            // 这就把authentication转化为json了，这个工具类很好用啊
            response.getWriter().write(objectMapper.writeValueAsString(authentication));
        } else {
            super.onAuthenticationSuccess(request,response,authentication);
        }



    }
}
