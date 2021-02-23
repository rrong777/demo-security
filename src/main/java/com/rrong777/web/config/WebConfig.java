package com.rrong777.web.config;

import com.rrong777.web.filter.TimeFilter;
import com.rrong777.web.interceptor.TimeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration 注解告诉Spring我们这是一个配置类 类似xml
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private TimeInterceptor timeInterceptor;
    /**
     * 继承WebMvcConfigurerAdapter
     * 重写下面这个方法。入参是拦截器的注册器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截器注释掉 测试其他功能
//        registry.addInterceptor(timeInterceptor);
    }

    /**
     * 这个方法用来配置异步支持的。
     * @param configurer
     */
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        // 下面这个方法是给Runnable方式的异步处理请求配置拦截器
        // 这个方法的入参类型是CallableProcessingInterceptor
        // 这个CallableProcessingInterceptor和我们之前讲的拦截器有相同的地方，
        // 有beforeConcurrentHandling 处理handler之前的逻辑
        // preProcess()
        // postProcess()这些都是有的，
        // 但是还有额外的。handleTimeout()  异步请求超时了是怎么处理的。
//        configurer.registerCallableInterceptors()
//        configurer.setDefaultTimeout() 设置异步处理请求的默认的超时时间 因为你是开了另一个线程去处理这个东西。
        // 这些线程有可能阻塞 有可能死掉。在多长时间内，这个多线程处理没有完毕，我们这个请求就返回给前端，这个是超时时间的一个设置
//        configurer.setTaskExecutor()
        // 由runnable去执行异步请求的时候，实际上Spring是用了自己的一个简易的异步线程池进行处理的。不是一个真正的线程池，
        // 不会重用里面的线程，每一次被调用都是新开一个新线程，你可以自己去设置一个线程池替代spring这种不可重用的简易线程池。
        // 下面是给DeferredResult方式异步处理请求配置拦截器
//        configurer.registerDeferredResultInterceptors()
        // 之前的注册拦截器，是使用下面的addIntegerceptors() 注册但是在异步处理请求的情况下，跟同步是不一样的，所以
        // 要使用上面两种方式
    }

    /**
     * 返回值表明我们要注册的bean的类型。
     * 下面这个 方法的作用和你在xml标签中去配置一个bean是一样的。
     */
    @Bean
    public FilterRegistrationBean timeFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        // 直接在过滤器上声明@Component注解注入到容器中和这种方式注册filter有一种不同的地方
        // 直接声明@Component注解的 过滤器是对所有请求生效，而这种方式可以指定filter对哪些url生效
        TimeFilter timeFilter= new TimeFilter();
        registrationBean.setFilter(timeFilter);
        List<String> urls = new ArrayList<>();
        urls.add("/*"); // 这里是对所有路径都起作用，实际中可以自己去指定哪些路径这个过滤器生效
        registrationBean.setUrlPatterns(urls);
        return registrationBean;
    }
}
