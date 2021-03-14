package com.rrong777.web.logout;

import com.rrong777.web.properties.SecurityProperties;
import com.rrong777.web.support.SimpleResponse;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RrrongLogoutSuccessHandler implements LogoutSuccessHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private String signOutUrl;

    // 这个工具类是new出来的 不是 注入的
    private ObjectMapper objectMapper = new ObjectMapper();
    public RrrongLogoutSuccessHandler(String signOutUrl) {
        this.signOutUrl = signOutUrl;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.info("退出成功！");
        // 退出有可能是同步的退出请求，也有可能是异步的ajax请求，那我就做一个如果用户在项目内有配置页面，那么就直接跳转到退出页面
        //  如果用户没有配置退出页面，我就认为她需要一个json格式返回，我就给他一个json
        if(StringUtils.isBlank(signOutUrl)) {
            // 这里是分号啊 哥哥
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse("退出成功 ")));
        } else {
            response.sendRedirect(signOutUrl);
        }
    }
}
