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
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

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
    @Autowired
    private InvalidSessionStrategy invalidSessionStrategy;

    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;
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
                        // session过期封装到一个类里面
                        .invalidSessionStrategy(invalidSessionStrategy)
                        .maximumSessions(securityProperties.getBrowser().getSession().getMaximumSessions()) // 这里为什么要两个and()  这里设置最大维护session数量，同一个用户只能同时存在一个session
                        // 封装session并发以后 后面session把前面session踢掉这种情况。
                        // invalidSessionStrategy 和 sessionInformationExpiredStrategy继承相同的父类，
                        // 处理逻辑都是类似的。看下当前引发跳转（session过期，或者session被踢）的请求是否是html结尾
                        // 是的话就跳转到你配置的session失效跳转的页面
                        .expiredSessionStrategy(sessionInformationExpiredStrategy) // 你可以实现一个这种接口
                        .maxSessionsPreventsLogin(securityProperties.getBrowser().getSession().isMaxSessionsPreventsLogin()) // 这句话的意思是当前用户已经在a机器上登录之后，在b机器上还想登录，那么这个登录行为会被阻止
                .and()
                .and()
                    .logout()
                    .logoutUrl("/signOut")  // url和handler是互斥的，但是只配置url 没有提供这个url的handler是可以和下面一起的。
                    // 如果你又配置了url，又自己提供了一个处理了这个url的handler 再下面配置logoutSuccessHandler 肯定有问题
                   // 如果只配置了url 退出会跳转到这个url ，然后用下面这个handler处理。
                    .logoutSuccessHandler(logoutSuccessHandler)
                    .deleteCookies("JSESSIONID") // 退出成功以后一个常见的操作就是把浏览器里面的cookie清除掉，deleteCookie，删除
                    // 请求cookie中的JSESSIONID
                .and()
                    .authorizeRequests()
                    .antMatchers(
                            SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                            SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
                            securityProperties.getBrowser().getLoginPage(),
                            SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX+"/*",
                            SecurityConstants.DEFAULT_SESSION_INVALID_URL,
                            "/oauth/authorize")
                            .permitAll()
                    .anyRequest()
                    .authenticated()
                .and()
                    .csrf().
                        disable();

    }
}
