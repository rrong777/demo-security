package com.rrong777.utils.code.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class ValidateCodeController {
    public static final String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";

    // 调用ValidateCodeGenerator 的实现，如果用户有自己的实现，就用用户自己的，如果没有，就用默认的实现，
    @Autowired
    private ValidateCodeGenerator imageCodeGenerator;

    // Spring操作session 的工具类
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
    @GetMapping("/code/image")
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ImageCode imageCode = imageCodeGenerator.generate(new ServletWebRequest(request));
        // 第一个参数将请求放到这个工具类，然后工具类从请求中拿session，第二个参数是我们放到session里面的东西的key,第三个就是我们的imageCode
        sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY, imageCode);
        // 把image往外写，写一个jpeg格式的文件，往response的输出留里面写
        ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
    }


}
