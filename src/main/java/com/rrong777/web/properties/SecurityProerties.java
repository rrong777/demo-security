package com.rrong777.web.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

// 下面这行注解的意思是，SecurityProperties这个类会读取 配置文件中  所有以 rrong777.security 开头的
@ConfigurationProperties(prefix = "rrong777.security")
public class SecurityProerties {
    // 其中 rrong777.security.browser 开头的配置项 都会映射到
    private BrowserProperties browser = new BrowserProperties();

    public BrowserProperties getBrowser() {
        return browser;
    }

    public void setBrowser(BrowserProperties browser) {
        this.browser = browser;
    }
}
