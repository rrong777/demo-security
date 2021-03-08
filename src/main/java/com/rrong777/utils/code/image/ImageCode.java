package com.rrong777.utils.code.image;

import com.rrong777.utils.code.sms.ValidateCode;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

// 新增短信验证码实体类之后，发现图像验证码只和短信验证码差一个image属性
public class ImageCode extends ValidateCode {
    // 图形
    private BufferedImage image;

    public ImageCode(BufferedImage image, String code, LocalDateTime expireTime) {
        // 如果子类构造器没有声明调用父类构造器的话，子类会先调用父类的无参构造器初始化父类中的参数.
        // 所以这里要显式声明调用，不然父类没提供无参构造器会报错
        super(code, expireTime);
        this.image = image;
    }
    public ImageCode(BufferedImage image,String code, int expireIn) {
        super(code, expireIn);
        this.image = image;
    }
    
    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

}
