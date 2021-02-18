package com.rrong777.web.config;

import com.rrong777.web.filter.TimeFilter;
import com.rrong777.web.interceptor.TimeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        registry.addInterceptor(timeInterceptor);
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
