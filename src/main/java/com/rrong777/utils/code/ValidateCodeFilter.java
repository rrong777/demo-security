package com.rrong777.utils.code;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
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
    private AuthenticationFailureHandler authenticationFailureHandler;
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    public AuthenticationFailureHandler getAuthenticationFailureHandler() {
        return authenticationFailureHandler;
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    public SessionStrategy getSessionStrategy() {
        return sessionStrategy;
    }

    public void setSessionStrategy(SessionStrategy sessionStrategy) {
        this.sessionStrategy = sessionStrategy;
    }

    // 这个方法里面写验证码验证逻辑
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 如果过来的是表单登录请求/authentication/form
        if(StringUtils.equals("/authentication/form", request.getRequestURI()) && StringUtils.equalsIgnoreCase(request.getMethod(), "post")) {
            try {
                // 如果是一个表单登录请求，我要去校验验证码。要去session里面拿东西，所以要传这个请求进去
                // 如果抛出异常了，我们要自个儿做一个处理
                validate(new ServletWebRequest(request));
            } catch (ValidateCodeException e) {
                // 如果捕获到异常，用我们自定义的失败处理器
                // 把错误信息json返回回去
                authenticationFailureHandler.onAuthenticationFailure(request, response ,e);
                // 捕获到异常就返回，过滤器链就不继续走了
                return;
            }
        }
        // 处理完的话继续走过滤器
        filterChain.doFilter(request, response);
    }
    public void validate(ServletWebRequest request) throws ServletRequestBindingException {
        // 从Session里面拿出我们的验证码
        ImageCode codeInSession = (ImageCode)sessionStrategy.getAttribute (request, ValidateCodeController.SESSION_KEY);

        // 从请求参数里面拿到imageCode这个参数 登录的时候验证码传过来的参数名就交imageCode
        String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),
                "imageCode");

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码的值不能为空");
        }

        if (codeInSession == null) {
            throw new ValidateCodeException("验证码不存在");
        }

        if (codeInSession.isExpried()) {
            sessionStrategy.removeAttribute(request, ValidateCodeController.SESSION_KEY);
            throw new ValidateCodeException("验证码已过期");
        }

        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }

        sessionStrategy.removeAttribute(request, ValidateCodeController.SESSION_KEY);

    }
}