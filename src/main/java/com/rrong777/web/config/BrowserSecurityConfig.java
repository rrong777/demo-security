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
                .loginPage("/rrong777-signIn.html") // 指定登录页面，登录的时候就会去找这么一个页面。（没有前后端分离）
                .loginProcessingUrl("/authentication/form") // 让UsernamePasswordAuthenticationFilter处理这个路径（告知这是登录认证的请求）
                // security 默认的配置就是下面五行代码
//        http.httpBasic() httpBasic认证
                .and()
                .authorizeRequests() // 请求需要认证
                .antMatchers("/rrong777-signIn.html").permitAll() // 放开访问控制 针对特定url不需要认证即可访问
                .anyRequest() // 任何请求
                .authenticated() // 进行认证
                .and().csrf().disable();
    }
}
