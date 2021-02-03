package com.rrong777.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.rrong777.dto.User;
import com.rrong777.dto.UserQueryCondition;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

/**
 * 1. @RestController 注解表明这个类提供RestApi
 * 2. @RequestMapping 及其变体
 *      @GetMapping
 *      @PostMapping
 *    映射http请求url到Java方法
 * 3. RequestParam 映射请求参数 到 Java方法的参数
 * 4. @PageableDefault 指定分页参数默认值
 * 5. @RequestParam(value="username") String nickname; 使用nickname去接收url中的参数username
 *      value和name是一样的，如果没有这两个属性要求请求中的参数和形参命名一致
 *      required 默认为true  一定要求要传， 可以为false
 *      defaultValue=“tom” 给默认值
 * 6. Controller的 形参可以直接入一个对象 前台传相应的参数（和属性名一致），Spring会自动帮我们塞到对象的对应属性上
 * 7. @PathVariable映射url片段到Java方法的参数
 * 8. 在url声明中使用正则表达式
 * 9. @JsonView控制json输出内容
 * 10. @JsonView注解，  query方法是批量查询，而getInfo是精准查询，方法权限可能不一样，虽然返回的都是User，但是我不希望query返回用户密码，
 *     而getInfo可以返回密码
 *     这个注解控制你返回相同对象的视图（在某个视图显示哪些字段）
 *        （1）.在实体类中使用接口声明多个视图（interface 就是 接口）
 *        （2）.使用@JsonView 在值对象的get方法上指定属性在某个视图返回
 *        （3）.在controller方法上指定 返回值 （User） 是返回哪个视图的
 * 11. @GetMapping 继承自@RequestMapping 声明了method = RequestMethod.GET
 * 12. @RequestBody 映射请求体到Java方法的参数
 */
@RestController
@RequestMapping(value="/user")
public class UserController {
    // @PageableDefault指定分页参数默认值
    // Pageable 这个是SpringData中的一个对象，就是分页对象
    // @PageableDefault 给 Pageable赋默认值
    @GetMapping
    @JsonView(User.UserSimpleView.class) // 查询方法指定简单视图
    public List<User> query(UserQueryCondition userQueryCondition, @PageableDefault(size = 10, page = 2, sort = "username, asc") Pageable pageable) {
        //  ReflectionToStringBuilder 反射的toString的工具，把Condition打印在控制台上
        System.out.println(ReflectionToStringBuilder.toString(userQueryCondition, ToStringStyle.MULTI_LINE_STYLE));
        List<User> users = new ArrayList<>();
        users.add(new User("tom","123"));
        users.add(new User("jack","123"));
        users.add(new User("tony","123"));

        return users;
    }

    // @PathVariable 将 RequestMapping 中声明的片段映射到方法入参中。id片段作为id参数的值传递过去
    // @PathVariable 的name和value属性的作用是一样的
    // :\\d+ 只能接收数字
    @RequestMapping(value = "/{id:\\d+}", method = RequestMethod.GET)
    // name会将 url中的id 传给形参ids
    // required 是否必须  如果不指定name参数，形参名一定要和表达式中的片段名{xx}对上
    @JsonView(User.UserDetailView.class) // getInfo方法指定 详细视图
    public User getInfo(@PathVariable(name = "id") String ids) {
        System.out.println(ids);
        User user = new User();
        user.setUsername("tom");
        user.setPassword("123");
        return user;
    }
}
