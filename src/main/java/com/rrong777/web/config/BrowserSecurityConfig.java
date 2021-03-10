package com.rrong777.web.config;

import com.rrong777.utils.code.ValidateCodeFilter;
import com.rrong777.utils.sms.SmsCodeFilter;
import com.rrong777.web.properties.SecurityProerties;
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
    private SecurityProerties securityProerties;
    // 做一个配置，登录成功以后使用我们自定义的登录成功处理器。而不用Spring默认的处理器（默认处理就是登录成功后就转发到引起
    // 登录的请求）。把自己写的注入进来。
    @Autowired
    private AuthenticationSuccessHandler rrongAuthenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler rrongAuthenticationFailureHandler;
    //  导入另一个security配置类
    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private DataSource dataSource;
    // 记住我功能最后做验证的时候还要去根据用户密码查询用户信息，所以要有一个UserDetailsService
    @Autowired
    private UserDetailsService userDetailsService;
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCryptPasswordEncoder是Security提供的一个PasswordEncoder接口的实现类
        // 如果你有自己定义的加密逻辑，你可以注入一个自己定义的PasswordEncoder的实现类即可。注入了之后就可以用这个进行加密了，
        // BCryptPasswordEncoder 这个很强大，同样一个密码，每次加密出来结果都是不一样的，随机生成一个盐，加密的时候把随机生成的盐混在你的密码串里面。
        // 每次判断的时候，可以用混在密文密码里面随机生成的盐，再反推回来，你当时加密的那个串是什么。即同一个123456 分别两次加密结果是不一样的
        // 这个推荐使用
        return new BCryptPasswordEncoder();
    }

    // 记住我功能需要 存储token到数据库，所以要提供数据源，还有tokenRepository的实现，
    // JdbcTokenRepositoryImpl 里面有sql脚本，我们可以自己实现，但是用他的就行了
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        // 需要一个数据源
        tokenRepository.setDataSource(dataSource);
        // 这个设置成true启动的时候会自动去创建表
//        tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setAuthenticationFailureHandler(rrongAuthenticationFailureHandler);
        validateCodeFilter.setSecurityProerties(securityProerties);
        validateCodeFilter.afterPropertiesSet();

        SmsCodeFilter smsCodeFilter = new SmsCodeFilter();
        smsCodeFilter.setAuthenticationFailureHandler(rrongAuthenticationFailureHandler);
        smsCodeFilter.setSecurityProerties(securityProerties);
        smsCodeFilter.afterPropertiesSet();

        // 下面无行做了一个最简单的security的配置，使用表单认证，对所有请求都要进行认证。
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()// 指定认证方式为表单认证
//                .loginPage("/rrong777-signIn.html") // 指定登录页面，登录的时候就会去找这么一个页面。（没有前后端分离）
                    .loginPage("/authentication/require") // 需要认证的请求全部指向一个控制器 去判断直接返回 html或者返回json
                    .loginProcessingUrl("/authentication/form") // 让UsernamePasswordAuthenticationFilter处理这个路径（告知这是登录认证的请求）
                    .successHandler(rrongAuthenticationSuccessHandler) // 登录成功以后就会使用我们自己写的这个登录成功的处理器来处理了。
                    .failureHandler(rrongAuthenticationFailureHandler)
                .and()
                    .rememberMe()
                    .tokenRepository(persistentTokenRepository())
                    .tokenValiditySeconds(securityProerties.getBrowser().getRemembermeSeconds())
                    .userDetailsService(userDetailsService)
                // security 默认的配置就是下面五行代码
//        http.httpBasic() httpBasic认证
                .and()
                .authorizeRequests() // 请求需要认证
//                .antMatchers("/rrong777-signIn.html").permitAll() // 放开访问控制 针对特定url不需要认证即可访问
                // 需要认证的都去这个控制器，然后决定返回json或者 登录页，登录页也不需要认证，如果请求的是登录页，就直接返回登录页让你登录
                .antMatchers("/authentication/require", "/authentication/mobile",
                                        securityProerties.getBrowser().getLoginPage(),
                                        "/code/*").permitAll()
                .anyRequest() // 任何请求
                .authenticated() // 进行认证
                .and().csrf().disable()
                .apply(smsCodeAuthenticationSecurityConfig); // 把这个配置类的配置也加到这后面来
    }
}
