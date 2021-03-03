package com.rrong777.utils.code;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * OncePerRequestFilter这个过滤器保证我们这个过滤器每次只会被调用一次
 */
public class ValidateCodeFilter extends OncePerRequestFilter {

    // 这个方法里面写验证码验证逻辑
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 如果过来的是表单登录请求/authentication/form
        if(StringUtils.equals("/authentication/form", request.getRequestURI()) && StringUtils.equalsIgnoreCase(request.getMethod(), "post")) {
            try {
                // 如果是一个表单登录请求，我要去校验验证码。要去session里面拿东西，所以要传这个请求进去
                // 如果抛出异常了，我们要自个儿做一个处理
                validate(new ServletWebRequest(request));
            } catch (Exception e) {

            }
        } else {
            // 如果不是表单登录请求
            filterChain.doFilter(request, response);
        }
    }
}
