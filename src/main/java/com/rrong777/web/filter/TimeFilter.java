package com.rrong777.web.filter;


import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.Date;

/**
 * 实现filter接口的就是过滤器
 * 让过滤器生效就是把这个类声明成一个Component 注册在Spring容器里面
 *
 * filter有一个问题，你只能拿到http中的请求和响应。（chain.doFilter()前后拿到的就是请求和响应了。handler处理完会到doFilter()后面的逻辑里去）
 * 实际上当前请求是由哪个Controller的哪个handler进行处理的，filter里是不知道的。Filter接口实际上是J2EE规范中定义的。
 * J2EE规范并不了解跟Spring相关的任何东西。而Controller和handler其实是SpringMVC中的概念。
 * 所以在filter里面，其实你是没办法知道请求是由哪个controller，哪个handler进行处理的、
 * 如果你需要这些信息，你需要使用interceptor进行处理。
 */
//@Component
public class TimeFilter implements Filter {
    /**
     * init为初始化
     * destroy为销毁
     * doFilter为过滤器逻辑
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("My time filter init !");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        System.out.println("My time filter start !");
        long start = new Date().getTime();
        chain.doFilter(request, response);
        long end = new Date().getTime();

        System.out.println("My time filter finish !");
    }

    @Override
    public void destroy() {
        System.out.println("My time filter destroy !");
    }
}
