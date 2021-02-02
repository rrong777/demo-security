package com.rrong777.web.controller;

import com.rrong777.dto.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 1. @RestController 注解表明这个类提供RestApi
 * 2. @RequestMapping 及其变体
 *      @GetMapping
 *      @PostMapping
 *    映射http请求url到Java方法
 * 3. RequestParam 映射请求参数 到 Java方法的参数
 * 4. @PageableDefault 指定分页参数默认值
 */
@RestController
public class UserController {
    @RequestMapping(value="/user", method = RequestMethod.GET)
    public List<User> query() {
        return null;
    }
}
