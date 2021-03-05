package com.rrong777.web.properties;

/**
 * 最下层的默认配置(验证码的应用级默认配置)
 * 使用安全模块的项目不做任何配置就是使用这个应用级配置。
 */
public class ImageCodeProperties {
    // 验证码图片宽度
    private int width = 67;
    // 验证码图片高度
    private int height = 23;
    // 验证码位数
    private int length = 4;
    // 验证码过期时间
    private int expireIn = 60;

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

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }
}
