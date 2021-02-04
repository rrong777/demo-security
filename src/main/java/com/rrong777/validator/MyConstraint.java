package com.rrong777.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义校验注解
 */
// Target表示你这个注解可以标注在什么样子的元素上面
//@Retention(RetentionPolicy.RUNTIME) 运行时的一个注解
// @Constraint java validation里面标准的一个注解 表明当前注解用于校验
// validatedBy 表明我当前这个注解的校验逻辑写在哪个类里面
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MyConstraintValidator.class)
public @interface MyConstraint {
    /**
     * message 没给默认值，使用这个注解的时候就必须要赋值
     * groups
     * payload
     * 三个参数必须有 在这个自定义的校验注解里面
     *
     * message为校验不通过时的消息
     * 下面两个都是hibernate.validator里面的概念，知道就好
     * @return
     *
     */
    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
