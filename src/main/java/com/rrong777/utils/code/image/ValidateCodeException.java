package com.rrong777.utils.code.image;


import org.springframework.security.core.AuthenticationException;

/**
 * AuthenticationException 是Security定义的一个异常，是所有的身份认证过程中抛出的一个异常的基类。
 */
public class ValidateCodeException extends AuthenticationException {
    private static final long serialVersionUID = 1887834691766972667L;

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
