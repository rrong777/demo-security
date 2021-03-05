package com.rrong777.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.rrong777.dto.User;
import com.rrong777.dto.UserQueryCondition;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
 * 13. 日期类型参数处理
 * 14. @Valid注解和BindingResult验证请求参数的合法性并处理校验结果
 *      dto 声明@NotBlank等校验注解，
 *      handler方法入参创建对象的时候@Valid 使校验注解生效
 */
@RestController
@RequestMapping(value="/user")
public class UserController {
    @GetMapping("/me")
//    public Object getCurrentUser(Authentication authentication) {
    //可以直接在controller入参里面入一个authentication，security会把认证信息对象塞给你

    // 但是考虑到我可能不需要所有的认证信息，我只要对应的用户信息，这里就可以用这个注解，把用户返回
    public Object getCurrentUser(@AuthenticationPrincipal UserDetails user) {
        // 这就可以拿到线程中的authentication对象。但是这样有点麻烦，
//        return SecurityContextHolder.getContext().getAuthentication();
        return user;
    }
    // @PageableDefault指定分页参数默认值
    // Pageable 这个是SpringData中的一个对象，就是分页对象
    // @PageableDefault 给 Pageable赋默认值
    @GetMapping
    @ApiOperation("用户查询服务") // 这个对于方法的描述会出现在swagger文档中，@ApiOperation
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

    /**
     * 这个handler和上面的handler是一样的。只不过是这个handler会抛出异常。测试SpingBoot如何对于程序中的运行时异常
     * 进行处理
     * @param userQueryCondition
     * @param pageable
     * @return
     */
//    @GetMapping
//    @JsonView(User.UserSimpleView.class) // 查询方法指定简单视图
//    public List<User> query(UserQueryCondition userQueryCondition, @PageableDefault(size = 10, page = 2, sort = "username, asc") Pageable pageable) {
//        throw new RuntimeException("user not  exist");
//    }

    // @PathVariable 将 RequestMapping 中声明的片段映射到方法入参中。id片段作为id参数的值传递过去
    // @PathVariable 的name和value属性的作用是一样的
    // :\\d+ 只能接收数字
//    @RequestMapping(value = "/{id:\\d+}", method = RequestMethod.GET)
//    // name会将 url中的id 传给形参ids
//    // required 是否必须  如果不指定name参数，形参名一定要和表达式中的片段名{xx}对上
//    @JsonView(User.UserDetailView.class) // getInfo方法指定 详细视图
//    public User getInfo(@PathVariable(name = "id") String ids) {
//        System.out.println(ids);
//        User user = new User();
//        user.setUsername("tom");
//        user.setPassword("123");
//        return user;
//    }

    @GetMapping("/{id:\\d+}")
    // name会将 url中的id 传给形参ids
    // required 是否必须  如果不指定name参数，形参名一定要和表达式中的片段名{xx}对上
    @JsonView(User.UserDetailView.class) // getInfo方法指定 详细视图  @ApiParam("用户id") 对于没有封装在对象中的请求参数，使用这个注解可以描述参数，在swagger文档中显示
    public User getInfo(@ApiParam("用户id") @PathVariable String id) {
//        throw new UserNotExistException(id); // 这个是测试 抛出异常到ControllerAdvice里面的
        System.out.println("进入getInfo服务");
        User user = new User();
        user.setUsername("tom");
        user.setPassword("123");
        return user;

    }

    /**
     * post请求的参数是在请求体里面的，请求体里面的参数想要通过形参列表直接接收需要使用@RequestBody注解，不然不会映射到handler的形参列表
     * get请求是通过url中的参数直接映射到形参列表的同名参数，@RequestParam可以省写，可以使用@RquestParam的value或者name属性指定 形参接收请求url中的某个参数
     *
     * 日期处理：前台如何传一个字符串过来 后台直接变成date类型
     *  前后台直接传递时间戳。前台决定时间戳如何展示，后台只负责提供一个时间戳，
     *
     *       * @Valid (// 校验，是否为空等)
     *      *  做得好一点就是封装一下，差一点就是复制，一旦复制，就GG了，一旦修改要修改很多地方。
     *      *  在DTO 实体类的参数声明上直接加一行 @NotBlank直接校验是否为空
     *      * @NotBlank是hibernate.validator这个项目提供的一个注解
     *      * @NotBlank 要和@Valid一起使用  要在创建这个对象的地方加上@Valid ，声明了hibernate.validator 里面的校验注解才会起作用
     *      *  而一般就是@RequestBody 请求过来的参数里面的需要校验，所以在handler声明@Valid
     *      *  此时如果password为null 返回400 请求格式错误
     *      *  BindingResult和@Valid配合，现在加上@Valid之后没过验证，直接不进handler，加上BindingResult之后会进入handler 并且带上错误信息进入Controller
     */
//    @PostMapping
//    public User create(@Valid @RequestBody User user, BindingResult errors) {
//        // 进入程序，是否有错误，
//        if(errors.hasErrors()) {
//            errors.getAllErrors().stream().forEach(error -> System.out.println(error.getDefaultMessage()));
//            // 此时在这里异常处理即可。这样子就不必反复的写校验逻辑，全部写在dto实体类里面就好了 ，其他有用到的地方都做校验即可
//            // 传了这个bindingResult就会进入Controller  这里要自己做处理
//        }
//        System.out.println(user.getUsername());
//        System.out.println(user.getPassword());
//        System.out.println(user.getId());
//        System.out.println(user.getBirthDay());
//        // 假设set完id  user就创建了
//        user.setId(1);
//        return user;
//    }

    /**
     * 这个handler和上面的handler是一样的，只是上面的handler可以进入，进入之后可以拿到错误消息等。
     * 这个controller直接拦住 还没有进入handler SpringBoot直接返回错误消息
     * @param user
     * @return
     */
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        // 进入程序，是否有错误，
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println(user.getId());
        System.out.println(user.getBirthDay());
        // 假设set完id  user就创建了
        user.setId(1);
        return user;
    }

    @PutMapping("/{id:\\d+}")
    public User update(@Valid @RequestBody User user, BindingResult errors) {
        // 进入程序，是否有错误，
        if(errors.hasErrors()) {
            /**
             * error到底是什么东西（ObjectError）
             */
            errors.getAllErrors().stream().forEach(error -> {
                FieldError fieldError = (FieldError) error;
                // 但是这样每次自己拼接也很麻烦，可以拿到未过校验的属性。直接在dto加了validator注解的属性上 加上message
                String message = fieldError.getField() + " " + error.getDefaultMessage();
                System.out.println(message);
            });
            // 此时在这里异常处理即可。这样子就不必反复的写校验逻辑，全部写在dto实体类里面就好了 ，其他有用到的地方都做校验即可
        }
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println(user.getId());
        System.out.println(user.getBirthDay());
        // 假设set完id  user就创建了
        user.setId(1);
        return user;
    }
    /**
     * 删除
     *
     * @param id
     */
    @DeleteMapping("/{id:\\d+}")
    public void delete(@PathVariable String id) {
        System.out.println(id);
    }
}
