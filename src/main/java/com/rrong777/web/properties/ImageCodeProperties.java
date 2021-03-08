package com.rrong777.web.properties;

/**
 * 最下层的默认配置(验证码的应用级默认配置)
 * 使用安全模块的项目不做任何配置就是使用这个应用级配置。
 *
 * 图形验证码和短信验证码配置上，很多属性都是重复的，那这个就可以继承短信验证码配置类，
 * 但是图形验证码默认长度是4，短信验证码默认长度是6
 */
public class ImageCodeProperties extends SmsCodeProperties{
    public ImageCodeProperties() {
        setLength(4);
    }

    // 验证码图片宽度
    private int width = 67;
    // 验证码图片高度
    private int height = 23;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
