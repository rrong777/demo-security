package com.rrong777.web.config;

import com.rrong777.utils.code.ValidateCodeFilter;
import com.rrong777.web.properties.SecurityProerties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *  WebSecurityConfigurerAdapter - SpringSecurity提供的一个适配器类，专门用来做web应用安全配置的适配器
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private SecurityProerties securityProerties;
    // 做一个配置，登录成功以后使用我们自定义的登录成功处理器。而不用Spring默认的处理器（默认处理就是登录成功后就转发到引起
    // 登录的请求）。把自己写的注入进来。
    @Autowired
    private AuthenticationSuccessHandler rrongAuthenticationHandler;
    @Autowired
    private AuthenticationFailureHandler rrongAuthenticationFailureHandler;
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCryptPasswordEncoder是Security提供的一个PasswordEncoder接口的实现类
        // 如果你有自己定义的加密逻辑，你可以注入一个自己定义的PasswordEncoder的实现类即可。注入了之后就可以用这个进行加密了，
        // BCryptPasswordEncoder 这个很强大，同样一个密码，每次加密出来结果都是不一样的，随机生成一个盐，加密的时候把随机生成的盐混在你的密码串里面。
        // 每次判断的时候，可以用混在密文密码里面随机生成的盐，再反推回来，你当时加密的那个串是什么。即同一个123456 分别两次加密结果是不一样的
        // 这个推荐使用
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setAuthenticationFailureHandler(rrongAuthenticationFailureHandler);
        // 下面无行做了一个最简单的security的配置，使用表单认证，对所有请求都要进行认证。
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()// 指定认证方式为表单认证
//                .loginPage("/rrong777-signIn.html") // 指定登录页面，登录的时候就会去找这么一个页面。（没有前后端分离）
                .loginPage("/authentication/require") // 需要认证的请求全部指向一个控制器 去判断直接返回 html或者返回json
                .loginProcessingUrl("/authentication/form") // 让UsernamePasswordAuthenticationFilter处理这个路径（告知这是登录认证的请求）
                .successHandler(rrongAuthenticationHandler) // 登录成功以后就会使用我们自己写的这个登录成功的处理器来处理了。
                .failureHandler(rrongAuthenticationFailureHandler)
                // security 默认的配置就是下面五行代码
//        http.httpBasic() httpBasic认证
                .and()
                .authorizeRequests() // 请求需要认证
//                .antMatchers("/rrong777-signIn.html").permitAll() // 放开访问控制 针对特定url不需要认证即可访问
                // 需要认证的都去这个控制器，然后决定返回json或者 登录页，登录页也不需要认证，如果请求的是登录页，就直接返回登录页让你登录
                .antMatchers("/authentication/require",
                                        securityProerties.getBrowser().getLoginPage(),
                                        "/code/image").permitAll()
                .anyRequest() // 任何请求
                .authenticated() // 进行认证
                .and().csrf().disable();
    }
}
