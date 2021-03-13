package com.rrong777.web.config;

import com.rrong777.validate.code.ValidateCodeFilter;
import com.rrong777.validate.code.ValidateCodeSecurityConfig;
import com.rrong777.validate.code.sms.SmsCodeFilter;
import com.rrong777.web.properties.SecurityConstants;
import com.rrong777.web.properties.SecurityProperties;
import com.rrong777.web.security.FormAuthenticationConfig;
import com.rrong777.web.session.Rrrong777ExpiredSessionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 *  WebSecurityConfigurerAdapter - SpringSecurity提供的一个适配器类，专门用来做web应用安全配置的适配器
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private AuthenticationSuccessHandler rrongAuthenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler rrongAuthenticationFailureHandler;
    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private DataSource dataSource;
    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;
    @Autowired
    private FormAuthenticationConfig formAuthenticationConfig;
    @Autowired
    private UserDetailsService userDetailsService;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 表单登录配置项
        formAuthenticationConfig.configure(http);

        http.apply(validateCodeSecurityConfig)
                .and()
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(securityProperties.getBrowser().getRemembermeSeconds())
                .userDetailsService(userDetailsService)
                .and()
                .sessionManagement()
                .invalidSessionUrl("/session/invalid") // 当session失效的时候我要跳转的一个地址
                .maximumSessions(1) // 这里为什么要两个and()  这里设置最大维护session数量，同一个用户只能同时存在一个session
                // 同一个用户后面登录产生的session会把前面登录所产生的session给失效掉。
                // 根据后面登录把前面登录踢掉的这种 情况，也是可以把他导入到一个url上做处理的，跳转的时候实际上你是把原来的请求给丢了。
                // 如果你想根据原来的请求做一个记录，比如说是谁把我给踢掉的。
                .expiredSessionStrategy(new Rrrong777ExpiredSessionStrategy()) // 你可以实现一个这种接口
                .maxSessionsPreventsLogin(true) // 这句话的意思是当前用户已经在a机器上登录之后，在b机器上还想登录，那么这个登录行为会被阻止
                .and()
                .and()
                .authorizeRequests()
                .antMatchers(
                        SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                        SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
                        securityProperties.getBrowser().getLoginPage(),
                        SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX+"/*",
                        "/session/invalid")
                        .permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();

    }
}
