package com.rrong777.utils.code.sms;

import com.rrong777.utils.code.ValidateCodeGenerator;
import com.rrong777.utils.code.image.ImageCode;
import com.rrong777.web.properties.SecurityProerties;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
// 短信验证码直接声明成component，不像图形验证码一样各种生成逻辑，就是短信服务商
@Component("smsCodeGenarator")
public class SmsCodeGenarator implements ValidateCodeGenerator {
    private SecurityProerties securityProerties;
    public ValidateCode generate(ServletWebRequest request) {
        // 短信验证码就是一个随机的纯数字随机字符串，长度要可配置
        String code = RandomStringUtils.randomNumeric(securityProerties.getCode().getSms().getLength());
        return new ValidateCode(code, securityProerties.getCode().getSms().getExpireIn());
    }

    public SecurityProerties getSecurityProerties() {
        return securityProerties;
    }

    public void setSecurityProerties(SecurityProerties securityProerties) {
        this.securityProerties = securityProerties;
    }
}
