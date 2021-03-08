package com.rrong777.utils.code;

import com.rrong777.utils.code.image.ImageCode;
import com.rrong777.utils.code.sms.SmsCodeGenarator;
import com.rrong777.utils.code.sms.SmsCodeSender;
import com.rrong777.utils.code.sms.ValidateCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;

@RestController
public class ValidateCodeController {
    public static final String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";

    // 调用ValidateCodeGenerator 的实现，如果用户有自己的实现，就用用户自己的，如果没有，就用默认的实现，
    @Autowired
    private ValidateCodeGenerator imageCodeGenerator;

    @Autowired
    private ValidateCodeGenerator smsCodeGenerator;

    @Autowired
    private SmsCodeSender smsCodeSender;

    @Autowired
    private SmsCodeGenarator smsCodeGenarator;

    // Spring操作session 的工具类
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    /**
     * 验证码创建的主干逻辑，都是三步，第一步是生成，第二步是放到session里面去，第三步是发送出去。发送逻辑可能不一样，图片验证码就是写到响应里面，短信验证码是调用短信服务商服务发送短信
     *
     * 像这种主干逻辑是相同的，但是个别步骤不同的，我们一般会用一种模板方法模式把代码抽象，然后就是把代码重构
     *
     * 所谓重构就是在保证代码行为不变的情况下，去优化代码
     * 在重构的过程中不会有任何功能的变化，原有的行为不会改变，只会让代码的质量变得更高，变得更容易扩展
     * 消除硬编码的字符串，提成公有的常量。把大块的代码拆分成小的可重用的方法。重构的过程中不会有任何功能的变化，只是让代码质量更高。
     *
     * 这块重构的思想就是用一个模板方法把他抽出来
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/code/image")
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ImageCode imageCode = (ImageCode) imageCodeGenerator.generate(new ServletWebRequest(request));
        // 第一个参数将请求放到这个工具类，然后工具类从请求中拿session，第二个参数是我们放到session里面的东西的key,第三个就是我们的imageCode
        sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY, imageCode);
        // 把image往外写，写一个jpeg格式的文件，往response的输出留里面写
        ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
    }
    @GetMapping("/code/sms")
    public void createSmsCode(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException {
        ValidateCode smsCode = smsCodeGenerator.generate(new ServletWebRequest(request));
        sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY, smsCode);
        // 请求中必须包含这个参数，
        String mobile = ServletRequestUtils.getRequiredStringParameter(request, "mobile");
        // 图形验证码是把验证码结果写到流里面，短信验证码是通过短信服务商发送到用户手上
        smsCodeSender.send(mobile, "123456");
    }


}
