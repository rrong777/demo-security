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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 测试用例
 * 好的代码都是在不断重构中完成的，有了测试用例之后，在你重构之后就可以让程序按照你期望的发展，去测试你重构后的代码，保证你重构后的代码不改变程序逻辑
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
        // size,page,sort 是SpringData 的Pageable的分页参数 请求中传参数
        String result = mockMvc.perform(get("/user")
                .param("username", "jojo")
                .param("age", "18")
                .param("ageTo", "60")
                .param("xxx", "111")
                .param("size", "15")
                .param("page", "3")
                .param("sort", "age,desc")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()) // 期望，我这个是测试用例，我一定有期望返回的结果.期望状态码是200
                .andExpect(jsonPath("$.length()").value(3))
                .andReturn().getResponse().getContentAsString(); // jsonPath是一个表达式（语法） $占位符，结果的长度是3
        // 执行这个测试用例，会发起一个请求，去测试响应
        // github搜索jsonpath会有相关的东西
        System.out.println(result);
    }

    // 点击这个类run as 整个类的测试用例都会被执行，点击这个方法run as 只运行着一个测试用例
    @Test
    public void whenGetInfoSuccess() throws Exception {
        String result = mockMvc.perform(get("/user/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("tom"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }

    // 请求传一个a 片段过去，测试用例是正确执行完毕的，但是controller返回的是一个错误结果
    @Test
    public void whenGetInfoFail() throws Exception {
        mockMvc.perform(get("/user/a")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is4xxClientError());

    }

    /**
     * 时间戳目前只有13位，我觉得到16位之后 程序已经不在用了，所以时间戳前台可以直接使用数值来传  不会经度损失
     * 前台数值超过16位就会精度损失。
     *
     * 前台传birthDay： 时间戳，会被Date birthDay 参数直接转换为 Java 日期格式，任何一门语言都有能处理时间戳 <->时间格式的能力
     * 后台传日期格式给前台，又直接变成时间戳了。
     * HttpResult 序列化的时候怎么办，会把日期直接序列化 toString 不会转成时间戳
     *
     * @throws Exception
     */
    @Test
    public void whenCreateSuccess() throws Exception {
        Date date = new Date();
        System.out.println(date.getTime());
        // 发送过去的内容   405 请求不匹配，但是有找到处理这个url的handler
        String content = "{\"username\":\"tom\", \"password\": null, \"birthDay\": "+date.getTime()+"}";
        System.out.println(content);
        String result = mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andReturn().getResponse().getContentAsString(); // 期望返回的id是1
        System.out.println(result);
    }

    /**
     * get 查
     * post 增
     * put 修改
     * delete 删除
     * @throws Exception
     */
    @Test
    public void whenUpdateSuccess() throws Exception {
        // LocalDateTime 1.8 新增的操作日期的api
        // plusYears() 操作时间 atZone 时区 为默认时区 拿到一个一年以后的时间当作生日传过去

        // must be in the past
        //may not be empty
        // BindingResult 绑定了如上两个错误，进入程序运行
        Date date = new Date(LocalDateTime.now().plusYears(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        System.out.println(date.getTime());
        // 发送过去的内容   405 请求不匹配，但是有找到处理这个url的handler
        String content = "{\"id\": 1,\"username\":\"tom\", \"password\": null, \"birthDay\": "+date.getTime()+"}";
        System.out.println(content);
        String result = mockMvc.perform(put("/user/1").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andReturn().getResponse().getContentAsString(); // 期望返回的id是1
        System.out.println(result);
    }

    /**
     * 删除测试用例，并没有判断 返回的content 因为RestFul是通过响应状态码判断 请求结果的，删除除了成功就是失败，不需要content
     * @throws Exception
     */
    @Test
    public void whenDeleteSuccess() throws Exception {
        mockMvc.perform(delete("/user/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }


}
