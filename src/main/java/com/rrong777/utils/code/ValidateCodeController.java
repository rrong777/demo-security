package com.rrong777.utils.code;

import com.rrong777.utils.code.image.ImageCode;
import com.rrong777.utils.code.sms.SmsCodeGenarator;
import com.rrong777.utils.code.sms.SmsCodeSender;
import com.rrong777.utils.code.sms.ValidateCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.Map;

@RestController
public class ValidateCodeController {
    @Autowired
    private Map<String, ValidateCodeProcessor> validateCodeProcessors;

    /**
     * 创建验证码，根据验证码类型不同，调用不同的ValidateCodeProcessor接口实现
     * @param request
     * @param response
     */
    @GetMapping("/code/{type}")
    public void createCode(HttpServletRequest request, HttpServletResponse response, @PathVariable String type) throws Exception {
        validateCodeProcessors.get(type + "CodeProcessor").create(new ServletWebRequest(request,response));
    }
}
