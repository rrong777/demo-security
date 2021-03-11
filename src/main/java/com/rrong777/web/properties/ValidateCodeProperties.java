package com.rrong777.web.properties;

/**
 * 验证码配置 - 验证码又分为图片验证码 和 手机验证码 所以在图片验证码上面再封装一层。
 */
public class ValidateCodeProperties {
    // new出来的对象就有默认的
    private ImageCodeProperties imageCode = new ImageCodeProperties();
    private SmsCodeProperties smsCode = new SmsCodeProperties();

    public ImageCodeProperties getImageCode() {
        return imageCode;
    }

    public void setImageCode(ImageCodeProperties imageCode) {
        this.imageCode = imageCode;
    }

    public SmsCodeProperties getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(SmsCodeProperties smsCode) {
        this.smsCode = smsCode;
    }
}
