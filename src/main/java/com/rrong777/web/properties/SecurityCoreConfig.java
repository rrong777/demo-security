package com.rrong777.web.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// 这个类是为了让在这个类所在包下的其他配置类（Properties）生效
@Configuration // 这是一个配置类
@EnableConfigurationProperties(SecurityProperties.class) // 这个配置类的作用是，让刚才写的SecurityProperties 这个配置的读取器生效
// 加了上面这行注解之后 SecurityProperties中的@ConfigurationProperties 这个注解就不会报错了。
// 做完这些配置以后，SecurityProperties 就可以去读取我们的application.yml中对应的配置了（rrong777.security开头的配置）
public class SecurityCoreConfig {

}
