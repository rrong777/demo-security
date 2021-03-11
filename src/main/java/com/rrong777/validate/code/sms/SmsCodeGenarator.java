package com.rrong777.validate.code.sms;

import com.rrong777.validate.code.ValidateCode;
import com.rrong777.validate.code.ValidateCodeGenerator;
import com.rrong777.web.properties.SecurityProperties;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

// 短信验证码直接声明成component，不像图形验证码一样各种生成逻辑，就是短信服务商
@Component("smsCodeGenerator")
public class SmsCodeGenarator implements ValidateCodeGenerator {
    @Autowired
    private SecurityProperties securityProperties;
    public ValidateCode generate(ServletWebRequest request) {
        // 短信验证码就是一个随机的纯数字随机字符串，长度要可配置
        String code = RandomStringUtils.randomNumeric(securityProperties.getValidateCode().getSmsCode().getLength());
        return new ValidateCode(code, securityProperties.getValidateCode().getSmsCode().getExpireIn());
    }

    public SecurityProperties getSecurityProperties() {
        return securityProperties;
    }

    public void setSecurityProperties(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }
}
