package com.rrong777.web.config;

import com.rrong777.web.jwt.RrrongJwtEnhancer;
import com.rrong777.web.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

@Configuration
public class TokenStoreConfig {
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    // 如果有配置存储是redis的，就用redis的。没配置或者配了jwt的 就使用jwt的方式
    @Bean
    @ConditionalOnProperty(prefix = "rrong777.security.oauth2", name="storeType", havingValue = "redis")
    public TokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * prefix = "rrong777.security.oauth2", name="storeType", havingValue = "jwt" 这三个的意思就是 application.yml中
     * rrong777.security.oauth2.storeType: jwt  // 如果有这么一个配置，下面这个类里面的配置都会生效
     *
     * matchIfMissing = true 如果没有配置 也是生效的，
     */
    @Configuration
    @ConditionalOnProperty(prefix = "rrong777.security.oauth2", name="storeType", havingValue = "jwt",matchIfMissing = true)
    public static class JwtTokenConfig {
        @Autowired
        private SecurityProperties securityProperties;
        @Bean
        public TokenStore jwtTokenStore() {
            // jwtTokenStore只管token的存取
            return new JwtTokenStore(jwtAccessTokenConverter());
        }

        /**
         * jwtAccessTokenConverter 有token生成的一些处理
         * jwt可以指定密钥，然后根据密钥进行签名，这个功能就是在jwtAccessTokenConverter里面实现的
         * @return
         */
        @Bean
        public JwtAccessTokenConverter jwtAccessTokenConverter() {
            JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
            // 设置签名使用的密钥
            accessTokenConverter.setSigningKey(securityProperties.getOauth2().getJwtSigningKey());
            return accessTokenConverter;
        }
        // 默认的。你有我就不用
        @Bean
        @ConditionalOnMissingBean(name="jwtTokenEnhancer")
        public TokenEnhancer jwtTokenEnhancer() {
            return new RrrongJwtEnhancer();
        }
    }
}
