package com.rrong777.utils.code;

import com.rrong777.utils.code.image.ImageCode;
import com.rrong777.web.properties.SecurityProerties;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * OncePerRequestFilter这个过滤器保证我们这个过滤器每次只会被调用一次
 * 实现InitializingBean这个接口的目的，是在其他参数都组装完毕之后，来初始化我们这个urls的值
 */
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {
    private AuthenticationFailureHandler authenticationFailureHandler;
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
    private SecurityProerties securityProerties;


    // 用来将 验证码配置类中的 url 存起来（原数据是以逗号分隔的字符串）
    private Set<String> urls = new HashSet<>();

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    // InitializingBean 的方法 重写
    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        String[] configUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(securityProerties.getCode().getImage().getUrl(), ",");
        if(configUrls != null) {
            for(String configUrl : configUrls) {
                urls.add(configUrl);
            }
        }
        // 登录请求是一定要做验证码的。
        urls.add("/authentication/form");
    }

    public SecurityProerties getSecurityProerties() {
        return securityProerties;
    }

    public void setSecurityProerties(SecurityProerties securityProerties) {
        this.securityProerties = securityProerties;
    }

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
        boolean action = false;
        // 在配置文件中配置了urls 这里就去检测拦截 urls
        for(String url : urls) {
            if(antPathMatcher.match(url, request.getRequestURI())) {
                action = true;
            }
        }

        // urls配置好了之后，这里就要加一个判断，判断是否我们的请求要加验证码验证逻辑。但是不可能用StringUtils.equals这种判断了， 因为我可能是/user/1 也可能是/user/2 后面这个
        // 是不确定的，要借助Spring的工具类，AntPatchers
        // 如果过来的是表单登录请求/authentication/form
        if(action) {
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
    private String getProcessorType(ServletWebRequest request){
        // 因为获取验证码的请求都是/code/开头的，   如果是图片验证码就是 /code/image  截取后半段，获得请求验证码的种类
        return StringUtils.substringAfter(request.getRequest().getRequestURI(), "/code/");
    }
    public void validate(ServletWebRequest request) throws ServletRequestBindingException {
        // 从Session里面拿出我们的验证码
        ImageCode codeInSession = (ImageCode)sessionStrategy.getAttribute (request, ValidateCodeProcessor.SESSION_KEY_PREFIX + "IMAGE");

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
            sessionStrategy.removeAttribute(request, ValidateCodeProcessor.SESSION_KEY_PREFIX + "IMAGE");
            throw new ValidateCodeException("验证码已过期");
        }

        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }

        sessionStrategy.removeAttribute(request, ValidateCodeProcessor.SESSION_KEY_PREFIX + "IMAGE");

    }
}
