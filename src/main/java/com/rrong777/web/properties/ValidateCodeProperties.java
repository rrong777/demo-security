package com.rrong777.web.properties;

/**
 * 验证码配置 - 验证码又分为图片验证码 和 手机验证码 所以在图片验证码上面再封装一层。
 */
public class ValidateCodeProperties {
    // new出来的对象就有默认的
    private ImageCodeProperties image = new ImageCodeProperties();
    private SmsCodeProperties sms = new SmsCodeProperties();

    public SmsCodeProperties getSms() {
        return sms;
    }

    public void setSms(SmsCodeProperties sms) {
        this.sms = sms;
    }

    public ImageCodeProperties getImage() {
        return image;
    }

    public void setImage(ImageCodeProperties image) {
        this.image = image;
    }
}
