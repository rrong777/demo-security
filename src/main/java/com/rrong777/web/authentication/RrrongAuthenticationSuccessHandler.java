package com.rrong777.web.authentication;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rrong777.web.properties.LoginType;
import com.rrong777.web.properties.SecurityProperties;
import jdk.nashorn.internal.parser.Token;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("rrongAuthenticationSuccessHandler") // 声明成一个Spring容器中的bean
//public class RrrongAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
// 此时不再实现这个认证成功处理器接口了，而是继承Security提供的默认的实现
public class RrrongAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SecurityProperties securityProperties;
    @Autowired // Spring启动的时候会自动为我们注入一个ObjectMapper
    private ObjectMapper objectMapper;

    /**
     * Spring容器管理的bean  SecurityOAuth2会自动注册，我们注入进来即可
     */
    @Autowired
    private ClientDetailsService clientDetailsService;

    // 注入这个 可以存储token还有token增强器
    @Autowired
    private AuthorizationServerTokenServices authorizationServerTokenServices;
    /**
     * 下面这个方法就是登录成功之后会呗调用
     * @param request
     * @param response
     * @param authentication Security的核心接口，封装了认证信息，包括你发起认证请求里面的信息，包括你发起认证请求的ip是多少。
     *                       session是什么
     *                       以及你认证通过以后我们自己写的UserDetails里面封装的用户信息。都是在这个Authentication对象里面的
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.info("登录成功");

        String header = request.getHeader("Authorization");
        // 请求头中必须要包含你的Basic开头的Authorization属性
        if (header == null || !header.startsWith("Basic ")) {
            throw new UnapprovedClientAuthenticationException("请求头中无client信息");
        }

        // extractAndDecodeHeader 提取并且解码我们请求头里面的 串
        String[] tokens = extractAndDecodeHeader(header, request);
        assert tokens.length == 2;

        String clientId = tokens[0];
        String clientSecret = tokens[1];

        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

        if (clientDetails == null) {
            throw new UnapprovedClientAuthenticationException("clientId对应的配置信息不存在：" + clientId);
            // 如果请求中的clientSecret和我配置的 ClientSecret不一致
        } else if(!StringUtils.equals(clientDetails.getClientSecret(), clientSecret)) {
            throw new UnapprovedClientAuthenticationException("clientSecret不匹配：" + clientId);

        }
        // 这个构造器四个参数，第一个参数就是用请求中的参数（因为每种模式是不一样的）。第一个参数是一个map
        // 这个map里装的就是每一种授权模式所需要的参数。SpringSecurityOAuth2会根据这些参数去组装Authentication参数。
        // 但是在这里 我们Authentication已经有了 已经是认证成功 要根据Authentication去生成一个AccessToken了
        // 所以第一个参数可以为空
        // 这个scope是配置好的， 因为就是在我们自己的app之间传送这个token  所以我就把所有的权限都给你就好了
        // 看源码也是为了 模仿生成token的过程。
        // 最后一个参数grant_type 我们就是自定义的
        TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_MAP, clientId, clientDetails.getScope(), "custom");

        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);

        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);

        OAuth2AccessToken token = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);


        // 改写我们的successHandler 并不需要处理我们页面的请求
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(token));




    }
    private String[] extractAndDecodeHeader(String header, HttpServletRequest request)
            throws IOException {

        byte[] base64Token = header.substring(6).getBytes("UTF-8");
        byte[] decoded;
        try {
            decoded = Base64.decode(base64Token);
        }
        catch (IllegalArgumentException e) {
            throw new BadCredentialsException(
                    "Failed to decode basic authentication token");
        }

        String token = new String(decoded, "UTF-8");

        int delim = token.indexOf(":");

        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }
        return new String[] { token.substring(0, delim), token.substring(delim + 1) };
    }
}
