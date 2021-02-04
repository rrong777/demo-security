package com.rrong777.validator;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 两个泛型，第一个泛型是表明你这个验证逻辑类（MyConstraintValidator） 要验证的注解是什么（MyConstraint） 就是说 表示这个验证逻辑是后面这个泛型的第一个类型的
 * Object  表明你要验证的属性是什么类型的（String username 你要验证username那直接写String） 这里写Object 可以验证任意类型
 * 如果写了String MyConstraint那么这个注解只有放在 String类型的变量上才会起作用
 */
public class MyConstraintValidator implements ConstraintValidator<MyConstraint, Object> {
//    @Autowired 在这里面你可以用Autowired填充你想要的东西。你继承了ConstraintValidator Spring就会管理，不用你来声明
    /**
     * 初始化方法
     * @param myConstraint
     */
    @Override
    public void initialize(MyConstraint myConstraint) {
        System.out.println("My validator init");
    }

    /**
     * 真正的校验逻辑
     * @param o 你传进来的这个校验的值
     * @param constraintValidatorContext 校验的上下文 包含了注解里面的值
     * @return
     */
    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        System.out.println(o);
        // false表示校验失败
        return false;
    }
}
