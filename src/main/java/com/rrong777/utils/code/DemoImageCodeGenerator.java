package com.rrong777.utils.code;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

// 自己注入一个  覆盖掉默认的验证码生成逻辑。但是用这个会抛空指针，因为没有返回，这里只是示范如何覆盖掉默认的逻辑
// 这个效果就像扩展Spring框架一样，自己定义一个接口，覆盖自己定义的这个接口的逻辑，高级的开发人员必须要掌握的一个技巧，以增量的方式去适应变化
// 图形验证码的这个逻辑改变了，原来这个不满足了，方式不是去改原来的代码
//@Component("imageCodeGenerator")
public class DemoImageCodeGenerator implements ValidateCodeGenerator{
    @Override
    public ImageCode generate(ServletWebRequest request) {
        System.out.println("更高级的验证码生成逻辑");
        return null;
    }
}
