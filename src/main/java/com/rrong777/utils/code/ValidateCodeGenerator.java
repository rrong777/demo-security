package com.rrong777.utils.code;

import com.rrong777.utils.code.image.ImageCode;
import com.rrong777.utils.code.sms.ValidateCode;
import org.springframework.web.context.request.ServletWebRequest;

public interface ValidateCodeGenerator {
    ValidateCode generate(ServletWebRequest request);
}
