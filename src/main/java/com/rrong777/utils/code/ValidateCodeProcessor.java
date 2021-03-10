package com.rrong777.utils.code;


import org.springframework.web.context.request.ServletWebRequest;

/**
 * 验证码处理器，封装不同的验证码处理逻辑
 */
public interface ValidateCodeProcessor {

    /**
     * 验证码放入session时的前缀
     */
    String SESSION_KEY_PREFIX = "SESSION_KEY_FOR_CODE_";


    /**
     * 创建校验码
     * ServletWebRequest 这个是Spring的一个工具类，封装请求和响应，虽然名字叫request但是这个工具类，请求和响应都可以封装在里面
     * 如果你的应用既需要request 又需要response 你可以封装在这里面传进去 不需要每次都传两个
     * @param servletWebRequest
     * @throws Exception
     */
    void create(ServletWebRequest servletWebRequest) throws Exception;

}
