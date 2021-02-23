package com.rrong777.web.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Aspect注解表明这是一个切面。
 * Component注解让这个切面生效
 */
// 拦截器注释掉 测试其他功能
//@Aspect
//@Component
public class TimeAspect {
    /**
     *      定义切入点。
     *      切入点包含两个东西（1）切入点表明在哪些方法生效（2）切入点表明在什么时候生效
     *
     *     @Before 在方法调用前
     *     @After 方法结束后
     *     @AfterThrowing 方法抛出异常时
     *     @AfterReturning 方法正常返回时
     *     @Around 环绕。  就像filter一样。 把整个handler运行过程包住
     *      通常使用Around  Around包含所有的
     *     execution("public * *(..)") 容器中所有public方法起作用
     *     execution("* set*(..)") 容器中所有以set开头命名的方法起作用。
     *     @Target是指你方法上有某个注解的时候起作用。 文档中都有声明，有时间可以去看看
     * @param pjp
     * @return
     */
    @Around("execution(* com.rrong777.web.controller.UserController.*(..))")  // @Around注解声明的是切入点
    public Object handleControllerMethod(ProceedingJoinPoint pjp) throws Throwable { // 这个方法就是增强。
        // pjp 对象包含你当前切面拦截住的那个方法的信息
        // 因为我这个是包围的。所以最后要把结果返回，这个结果可能是任意的类型（Object接收）
        System.out.println("Time Aspect Start");
        Object[] args = pjp.getArgs();
        for(Object arg : args) {
            System.out.println("args is :" + arg);
        }
        long start = new Date().getTime();

        // 下面这个方法就是去调用切面拦截住的控制器的那个方法。
        // 你handler的返回值是什么，这里的result就是什么，我们可以原样返回，也可以做一个预处理。
        Object result = pjp.proceed(); // 这个方法类似过滤器dofilter()去调用后面的过滤器链、
        System.out.println("Time Aspect 耗时：" + (new Date().getTime() - start));
        System.out.println("Time Aspect End");
        return result;
    }
}