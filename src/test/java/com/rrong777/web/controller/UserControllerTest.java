package com.rrong777.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

// 静态导入 直接调用
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 测试用例
 */
// 需要在这个类上面加一些注解，告诉SpringBoot这是一个测试用例

// 如何来运行这个测试用例，使用SpringRunner来执行这个测试用例
@RunWith(SpringRunner.class)
// 说明这整个类是一个测试用例
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private WebApplicationContext wac;
    // 测试的是web环境，要伪造一个MVC环境 伪造的环境并不会真正的去启动tomcat  非常快
    private MockMvc mockMvc;

    // 写了bofore注解的方法会在每一个测试用例运行前运行
    @Before
    public void setUp() {
        // 构建mockMvc， 讲wac 容器放进去构建
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void whenQuerySuccess() throws Exception {
        // perform 执行
        // 发一个模拟的请求，判断请求返回的响应是不是符合我们的期望，
        // MockMvcRequestBuilders.get("") 发出一个get请求.使用utf8的编码去发一个content-type是application-json的get请求
        // 这里的get(), status(),jsonPath() 都是静态导入
        mockMvc.perform(get("/user").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()) // 期望，我这个是测试用例，我一定有期望返回的结果.期望状态码是200
                .andExpect(jsonPath("$.length()").value(3)); // jsonPath是一个表达式（语法） $占位符，结果的长度是3
        // 执行这个测试用例，会发起一个请求，去测试响应
    }

}
