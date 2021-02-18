package com.rrong777.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @ControllerAdvice 这个注解表明当前这个类都是处理其他controller里面跑出来的异常的，本身并不处理http请求，只处理
 * 其他Controller抛出来的异常。
 */
@ControllerAdvice
public class ControllerExceptionHandler {
    /**
     * @ExceptionHandler注解表明当前方法处理的是哪个异常。
     * 当你其他Controller抛出UserNotExistException 异常的时候就会进入这个方法进行处理。
     * 方法入参就是对应的异常，
     * 此时程序抛出UserNotExistException，直接处理handler里面的异常，然后返回信息给前台。
     * @return
     */
    @ExceptionHandler(UserNotExistException.class)
    @ResponseBody // 这个注解说明把返回值转成对应的json数据。
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 服务器异常状态码
    public Map<String, Object> handleUserNotExistException(UserNotExistException ex){
        Map<String, Object> result = new HashMap<>();
        result.put("id", ex.getId());
        result.put("message", ex.getMessage());
        return result;
    }
}
