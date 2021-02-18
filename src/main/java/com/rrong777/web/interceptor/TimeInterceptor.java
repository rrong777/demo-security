package com.rrong777.web.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 声明成容器组件
 */
@Component
public class TimeInterceptor implements HandlerInterceptor {
    /**
     * 在handler方法被调用之前运行
     * 在过滤器里面，过滤器的逻辑都在doFilter()方法里面了。
     * 调用handler之前怎么处理，在chain.doFilter()前面，调用handler之后怎么处理，在chain.doFilter()之后。一个方法里面就可以完成
     * 在handler前后的处理
     *
     * 而过滤器是分成两部分的，preHandle() 是在handler调用之前的逻辑。postHandle()是在handler调用之后的逻辑
     * 如果这两个逻辑里面要传参的话要使用请求把参数带过去
     * 拦截器 比 filter有优势的一个地方就是这个 Object handler入参。
     * 这个handler入参是你用来处理当前request的handler的方法声明对象。
     *
     * preHandle()这个方法返回的是false 所以就进入不了后面的handler
     * preHandle()返回的是false  之后的postHandle()和aafterCompletion()也不会调用了（handler正常结束会
     * 调用postHandle()handler抛出异常会调用afterCompletion()  现在prehandle()返回的是false）
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        System.out.println("preHandle");

        // 打印handler类名
        System.out.println(((HandlerMethod)handler).getBean().getClass().getName());
        // 打印handler方法名
        System.out.println(((HandlerMethod)handler).getMethod().getName());
        httpServletRequest.setAttribute("start", new Date().getTime());
        return true;
    }

    /**
     * handler被调用之后，这个方法会被调用。
     * 如果handler中抛出了异常，postHandle()方法就不会被调用了。
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle");
        long start = (Long) httpServletRequest.getAttribute("start");
        System.out.println("请求耗时：" + (new Date().getTime() - start));
    }

    /**
     * afterCompletion() 不管你的handler是正常运行结束还是抛出异常都会被调用。
     * 不管成功还是失败都会进入这个方法，成功的时候Exception 入参是空的，
     * 当有异常进入这个方法的时候 Exception 入参是有值的
     *
     * @ControllerAdvice 处理异常是在afterCompletion()这个方法之前的，异常被处理掉了
     * 就相当于没有这个异常了（被捕获了就不会继续往上抛了）  这里的异常入参就会是空的
     * 在afterCompletion()之前没有被处理掉的异常才会进入这里被处理掉。
     *
     * 拦截器会拦截所有controller里面的方法调用，不光是你自己写的controller SpringBoot里面的controller也会被拦截
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object handler,
                                Exception e) throws Exception {
        // 如果异常处理了 比如UserNotExistException 被 @ControllerAdvice处理了。正常和抛出异常的时候都会到这里。你异常处理完
        // 了再到这里，就跟正常的没两样了。e 就是空的。记住@ControllerAdvice 处理是在这个方法之前就好了。
        System.out.println("afterCompletion");
        long start = (Long) httpServletRequest.getAttribute("start");
        System.out.println("请求耗时：" + (new Date().getTime() - start));
    }
}
