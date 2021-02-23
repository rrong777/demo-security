package com.rrong777;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@RestController // 提供rest服务的一个类
@EnableSwagger2 // 开启swagger功能 加入pom还有在主程序类中加入这个注解就可以开启swagger了
// http://localhost:8070/swagger-ui.html 访问这个链接 里面列出了系统里面所有的controller和endpoint
// endpoint是springmvc自己的一些控制器和端点。他们都是用来处理http请求的。可以先不去管这些spring默认的东西。
// 看我们自己写的
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping("/hello")
    public String hello () {
        return "hello Spring";
    }
}
