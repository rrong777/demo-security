package com.rrong777.web.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

// 下面这行注解的意思是，SecurityProperties这个类会读取 配置文件中  所有以 rrong777.security 开头的
// properties配置类就是默认配置，配置类的默认属性就是默认配置
// 而配置文件application.yml中的配置就是应用级别配置，代码不修改，打好包就不改变了，是默认配置，而配置文件可以修改
@ConfigurationProperties(prefix = "rrong777.security")
public class SecurityProperties {
    // 其中 rrong777.security.browser 开头的配置项 都会映射到
    private BrowserProperties browser = new BrowserProperties();

    private ValidateCodeProperties validateCode = new ValidateCodeProperties();
    public BrowserProperties getBrowser() {
        return browser;
    }

    public void setBrowser(BrowserProperties browser) {
        this.browser = browser;
    }

    public ValidateCodeProperties getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(ValidateCodeProperties validateCode) {
        this.validateCode = validateCode;
    }
}
