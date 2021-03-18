package com.rrong777.web.config;

import com.rrong777.web.properties.OAuth2ClientProperties;
import com.rrong777.web.properties.SecurityProperties;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

/**
 * 使用EnableAuthorizationServer注解的时候
 * 当你这个类不继承AuthorizationServerConfigurerAdapter 这个类的时候，Security自动会帮你注入authenticationManager
 * 和userDetailsService 帮你使用默认配置，但是你继承了这个AuthorizationServerConfigurerAdapter
 * 就表名你要自己配置认证服务器，所以你就需要自己注入 自己配置了
 *
 * 现在这些配置 已经符合Oauth2协议了，已经可以说是一个认证服务器了。
 * 单点登录很简单啊，在资源服务器上加一个读取认证方式就好了
 */
@Configuration
@EnableAuthorizationServer
public class RrrongAuthenticationServerConfig extends AuthorizationServerConfigurerAdapter {
    /**
     * authenticationManager 做认证的
     */
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);
    }

    /**
     * 可以配置有哪些应用会来访问我们的认证服务器（我们给哪些应用发clientId secret）
     * 你覆盖了这个方法，你在application.yml里面配置的clientId和clientSecret就不再起作用了
     * 会根据这个方法决定你能给哪些应用发令牌
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.inMemory(). 可以一直 . 链式调用下去，也可以用返回值来调用 本来链式调用就是上一个方法返回值去调用下一个调用
        // 令牌是存在内存当中的，一旦服务重启 令牌就消失了
        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
        if(ArrayUtils.isNotEmpty(securityProperties.getOauth2().getClients())) {
            for(OAuth2ClientProperties client : securityProperties.getOauth2().getClients()) {
                builder.withClient(client.getClientId())
                        .secret(client.getClientSecret())
                        .accessTokenValiditySeconds(client.getAccessTokenValiditySeconds())
                        .authorizedGrantTypes("refresh_token", "password") // 不希望用户配置的 可以直接写死在这里
                        .scopes("all","read","write");
            }
        }


    }

    /**
     * TokenEndpoint 是处理oauth Token的入口
     * 这里的endpoints就表示是TokenEndpoint入参，这个就是用来处理/Oauth/token，就是请求里用token去认证，而不是用session里的认证信息
     * 这个很基础啊，虽然现在不用，但是你要知道，浏览器session是这么一回事。
     * 执行一系列的业务逻辑，包括传下来的这个code或者用户名密码。去取用户信息等等。
     *
     * configure(AuthorizationServerEndpointsConfigurer endpoints)
     * 这个方法是针对端点的一些配置
     *
     * configure(AuthorizationServerSecurityConfigurer security)
     * 针对安全性的一些配置
     *
     * configure(ClientDetailsServiceConfigurer clients)
     * 针对第三方应用客户端的一些配置
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }
}
