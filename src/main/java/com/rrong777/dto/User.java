package com.rrong777.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.rrong777.validator.MyConstraint;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Past;
import java.util.Date;

/**
 * 常用的验证注解
 * Bean Validation 中内置的 constraint
 * @Null 被注释的元素必须为 null
 * @NotNull 被注释的元素必须不为 null
 * @AssertTrue 被注释的元素必须为 true
 * @AssertFalse 被注释的元素必须为 false
 * @Min(value) 被注释的元素必须是一个数字，其值必须大于等于指定的最小值
 * @Max(value) 被注释的元素必须是一个数字，其值必须小于等于指定的最大值
 * @DecimalMin(value) 被注释的元素必须是一个数字，其值必须大于等于指定的最小值
 * @DecimalMax(value) 被注释的元素必须是一个数字，其值必须小于等于指定的最大值
 * @Size(max=, min=)   被注释的元素的大小必须在指定的范围内
 * @Digits (integer, fraction)     被注释的元素必须是一个数字，其值必须在可接受的范围内 integer 指定整数最大长度  fraction指定小数部分最大长度
 * @Past 被注释的元素必须是一个过去的日期
 * @Future 被注释的元素必须是一个将来的日期
 * @Pattern(regex=,flag=) 被注释的元素必须符合指定的正则表达式
 * Hibernate Validator 附加的 constraint
 * @NotBlank(message =)   验证字符串非null，且长度必须大于0
 * @Email 被注释的元素必须是电子邮箱地址
 * @Length(min=,max=) 被注释的字符串的大小必须在指定的范围内
 * @NotEmpty 被注释的字符串的必须非空
 * @Range(min=,max=,message=) 被注释的元素必须在合适的范围内
 */
public class User {
    public interface UserSimpleView{};
    public interface UserDetailView extends UserSimpleView {};
    private Integer id;

    // @Valid这里创建这个user的时候，在UserController这里  发现这个注解，就会去调用校验逻辑，把我们这里这个值传进去
    @MyConstraint(message = "测试校验注解")
    private String username;
    /**
     * 校验是否为空
     * message属性，校验失败的时候  提示消息
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    @Past
    private Date birthDay;

    @JsonView(UserSimpleView.class)
    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    @JsonView(UserSimpleView.class)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @JsonView(UserSimpleView.class)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // 指定密码在详细视图展示，但是详细视图继承自简单视图，展示详细视图的时候会把简单视图的属性一起展示了
    @JsonView(UserDetailView.class)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
