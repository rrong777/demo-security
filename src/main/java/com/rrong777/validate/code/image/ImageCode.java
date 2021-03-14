package com.rrong777.validate.code.image;


import com.rrong777.validate.code.ValidateCode;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * 图片验证码
 * BufferedImage 这个对象实际上是没有实现Serializable 接口的，这个又是jdk提供给我们的，但是你想，我们存放到session的时候，只把
 * 验证码和过期时间存进去不就好了。图片存进去干嘛
 * spring-session自动帮我们完成了session在redis里面的维护。
 */
public class ImageCode extends ValidateCode {

    private static final long serialVersionUID = -500010999504413020L;

    private BufferedImage image;

    public ImageCode(BufferedImage image, String code, int expireIn){
        super(code, expireIn);
        this.image = image;
    }

    public ImageCode(BufferedImage image, String code, LocalDateTime expireTime){
        super(code, expireTime);
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

}

