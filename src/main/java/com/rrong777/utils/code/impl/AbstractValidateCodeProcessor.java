package com.rrong777.utils.code.impl;

import com.rrong777.utils.code.ValidateCodeGenerator;
import com.rrong777.utils.code.ValidateCodeProcessor;
import com.rrong777.utils.code.sms.ValidateCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;


public abstract class AbstractValidateCodeProcessor<C extends ValidateCode>
        implements ValidateCodeProcessor {

    /**
     * 操作session的工具类
     */
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    /**
     * 收集系统中所有的 {@link ValidateCodeGenerator} 接口的实现。
     * Spring看到注入这样一个map以后，会查找Spring容器里面所有的ValidateCodeGenerator 接口的实现找到了以后把bean 的名字为key，bean为value放到这个map中
     * 依赖搜索，定向的去搜索某一个接口的具体实现
     */
    @Autowired
    private Map<String, ValidateCodeGenerator> validateCodeGenerators;



    /**
     * 创建验证码
     * 在这个抽象实现里面，实现了这个create方法，主干逻辑里面的三步，生成，保存，发送
     * @param request
     * @throws Exception
     */
    @Override
    public void create(ServletWebRequest request) throws Exception {
        C validateCode = generate(request);
        save(request, validateCode);
        send(request, validateCode); // 发送是一个抽象方法，实现的时候自己去调用就好了。抽象类有两个子类，一个是图片的一个是短信的
        // 图片的实现发送方法就是把图片验证码写到响应中。短信验证码实现的时候就是用短信的发送器把短信发出去即可
    }

    /**
     * 生成校验码
     * 生成需要讲一下，涉及到Spring的一个开发技巧，叫做依赖查找 图形验证码和短信验证码的生成逻辑，都是封装到ValidateCodeGenerator这个接口
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    private C generate(ServletWebRequest request) {
        String type = getProcessorType(request);
        // 根据请求type去Map中获取一个 验证码生成器
        ValidateCodeGenerator validateCodeGenerator = validateCodeGenerators.get(type + "CodeGenerator");
        // 找出来之后调用这个生成方法即可
        return (C) validateCodeGenerator.generate(request);
    }

    /**
     * 保存校验码
     *
     * @param request
     * @param validateCode
     */
    private void save(ServletWebRequest request, C validateCode) {
        // 只将验证码的验证码和过期时间存入session
        ValidateCode code = new ValidateCode(validateCode.getCode(), validateCode.getExpireTime());
        sessionStrategy.setAttribute(request, SESSION_KEY_PREFIX + getProcessorType(request).toUpperCase(), validateCode);
    }


    /**
     * 发送校验码，由子类实现
     *
     * @param request
     * @param validateCode
     * @throws Exception
     */
    protected abstract void send(ServletWebRequest request, C validateCode) throws Exception;

    private String getProcessorType(ServletWebRequest request){
        // 因为获取验证码的请求都是/code/开头的，   如果是图片验证码就是 /code/image  截取后半段，获得请求验证码的种类
        return StringUtils.substringAfter(request.getRequest().getRequestURI(), "/code/");
    }

}
