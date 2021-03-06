package com.rrong777.utils.code;

import org.springframework.web.context.request.ServletWebRequest;

public interface ValidateCodeGenerator {
    ImageCode generate(ServletWebRequest request);
}
