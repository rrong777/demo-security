package com.rrong777.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 *  WebSecurityConfigurerAdapter - SpringSecurity提供的一个适配器类，专门用来做web应用安全配置的适配器
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 下面无行做了一个最简单的security的配置，使用表单认证，对所有请求都要进行认证。
        http.formLogin()// 指定认证方式为表单认证
        // security 默认的配置就是下面五行代码
//        http.httpBasic() httpBasic认证
                .and()
                .authorizeRequests() // 请求需要认证
                .anyRequest() // 任何请求
                .authenticated(); // 进行认证
    }
}
