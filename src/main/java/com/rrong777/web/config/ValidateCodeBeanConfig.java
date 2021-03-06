package com.rrong777.web.config;

import com.rrong777.utils.code.ImageCodeGenarator;
import com.rrong777.utils.code.ValidateCodeGenerator;
import com.rrong777.web.properties.SecurityProerties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidateCodeBeanConfig {
    @Autowired
    private SecurityProerties securityProerties;

    // 这个配置效果和 在ImageCodeGenarator 上声明一个@Component注解是一样的。但是为什么要这么配置呢？  因为这样子还能加一个注解
    @Bean
    // 加上下面这个注解之后，他会先去Spring容器中去找，找Spring容器中是否已经有一个名为imageCodeGenerator的bean，如果能找到就不会用下面这个，而是用容器中的imageCodeGenerator
    // 当容器中不存在imageCodeGenerator这样一个bean的时候，才会用下面这个注入容器
    @ConditionalOnMissingBean(name = "imageCodeGenerator")
    public ValidateCodeGenerator imageCodeGenerator() {
        ImageCodeGenarator imageCodeGenarator = new ImageCodeGenarator();
        imageCodeGenarator.setSecurityProerties(securityProerties);
        return imageCodeGenarator;
    }
}
