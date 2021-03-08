package com.rrong777.utils.code.sms;

import com.rrong777.utils.sms.SmsUtils;

import java.time.LocalDateTime;

/**
 * @author wuqr
 * @Title: 短信验证码实体类
 * @Description: 封装短信验证码信息
 * @date 2020/4/19 13:51
 * 但是图片验证码要继承这个类，因为图片验证码只是多了一个图片属性，图片验证码继承
 * smscode又不合适，所以改名叫ValidateCode
 */
public class ValidateCode {
    // 短信验证码
    private String code;
    // 短信验证码过期时间
    public LocalDateTime expireTime;

    public ValidateCode(String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public ValidateCode(String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }

    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    /**
     * 判断当前验证码对象是否过期
     * @return
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.getExpireTime());
    }

    public static void main(String[] args) {
        String code = VerificationCodeUtils.generateVerificationCode();
        ValidateCode smsCode = new ValidateCode(code, 60);
        SmsUtils.SendVerificationCodeBySms("13290981965", smsCode.getCode());

    }
}
