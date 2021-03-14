package com.rrong777.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * 资源服务器配置类
 * 作为资源服务器 ，现在用的东西还都是默认的配置。
 */
@Configuration
@EnableResourceServer
public class RrrongResourceServerConfig {

}
