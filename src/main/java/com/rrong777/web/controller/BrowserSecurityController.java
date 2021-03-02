package com.rrong777.web.controller;

import com.rrong777.web.properties.SecurityProerties;
import com.rrong777.web.support.SimpleResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class BrowserSecurityController {
    // 请求的缓存
    private RequestCache requestCache = new HttpSessionRequestCache();
    // Spring的一个工具，
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private SecurityProerties securityProerties;
    /**
     * 当需要身份认证时，跳转到这里
     * 比如 访问/user还没登录，先跳转到这里，给前台返回错误信息。
     * 在这里面要做一个判断。这个判断是引发跳转的是一个html请求还是，普通的请求（类似/user）
     * 这我们就需要拿到引发跳转的那个请求。
     * 接到请求以后，是否需要身份认证，这个判断是Security做的 如果需要身份认证，会根据SecurityConfig里面配置的loginPage做一个跳转。
     * 但是这个跳转相当于又是一个新的请求了（“authentication/require”），执行这个跳转之前，Security用HttpSessionRequestCache 这个类
     * 把当前的请求缓存到这个session里面去，所以跳转到“authentication/require” 这个请求以后，我们可以用HttpSessionRequestCache，再从session
     * 里面把刚才缓存的请求再拿出来。
     *
     * 这个控制器专门用来处理需要登录的请求，就是登录页其实也是需要登录（直接请求rrong777-signIn.html（或者换一个请求 请求 xxx.html）也需要登录，但是是直接请求html，
     * 来到这里会判断这个请求.请求的就是html 然后又需要登录，所以会返回rrong777-signIn.html） 这里其实就是多一层判断，让我们的程序更符合restful规范。
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/authentication/require")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED) // 返回未授权的状态码
    public SimpleResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Logger logger = LoggerFactory.getLogger(getClass());
        // 刚刚缓存的请求。就是需要登录，引发跳转到这个控制器的请求
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if(savedRequest != null) {
            // 引发跳转的请求的url
            String targetUrl = savedRequest.getRedirectUrl();
            logger.info("引发跳转的请求：" + targetUrl);
            // 如果引发跳转的请求是html
            if(StringUtils.endsWithIgnoreCase(targetUrl, ".html")) {
                // 第三个参数就是要跳转的url。这里就是第二个问题，不可能永远的跳转到标准登录页（有app 有web 我想自定义，
                // 我如果自定义了就到我自定义的登录页去，没有自定义就到你这个标准登录页） 我要让别人可以配置
                redirectStrategy.sendRedirect(request, response,securityProerties.getBrowser().getLoginPage());
            }
        }
        return new SimpleResponse("访问的服务需要身份认证！");
    }
}
