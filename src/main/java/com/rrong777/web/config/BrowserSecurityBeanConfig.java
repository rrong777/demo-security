package com.rrong777.web.config;

import com.rrong777.web.logout.RrrongLogoutSuccessHandler;
import com.rrong777.web.properties.SecurityProperties;
import com.rrong777.web.session.Rrrong777ExpiredSessionStrategy;
import com.rrong777.web.session.RrrongInvalidSessionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

/**
 * session相关的session配置bean
 *
 * 可以通过demo项目实现接口覆盖
 *
 * 浏览器环境下扩展点配置，配置在这里的bean，业务系统都可以通过声明同类型或同名的bean来覆盖安全
 * 模块默认的配置。
 */
@Configuration
public class BrowserSecurityBeanConfig {

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * session失效时的处理策略配置
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(InvalidSessionStrategy.class)
    public InvalidSessionStrategy invalidSessionStrategy() {
        return new RrrongInvalidSessionStrategy(securityProperties.getBrowser().getSession().getSessionInvalidUrl());
    }
    /**
     * 退出成功处理器
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(LogoutSuccessHandler.class)
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new RrrongLogoutSuccessHandler(securityProperties.getBrowser().getSignOutUrl());
    }

    /**
     * 并发登录导致前一个session失效时的处理策略配置
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SessionInformationExpiredStrategy.class)
    public SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
        return new Rrrong777ExpiredSessionStrategy(securityProperties.getBrowser().getSession().getSessionInvalidUrl());
    }

}