package com.rrong777.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

public interface ValidateCodeGenerator {
    /**
     * 图形验证码实现方法接口
     * @param request
     * @return
     */
    ValidateCode generate(ServletWebRequest request);
}
