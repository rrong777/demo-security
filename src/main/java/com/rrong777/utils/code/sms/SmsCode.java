package com.rrong777.utils.code.sms;

import com.rrong777.utils.sms.SmsUtils;

import java.time.LocalDateTime;

/**
 * @author wuqr
 * @Title: 短信验证码实体类
 * @Description: 封装短信验证码信息
 * @date 2020/4/19 13:51
 */
public class SmsCode {
    public SmsCode(String code) {
        this.code = code;
        this.setExpireTime();
    }

    // 短信验证码
    private String code;
    // 短信验证码过期时间：60秒
    public static final int EXPIRE_IN = 60;
    // 短信验证码过期时间
    public LocalDateTime expireTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime() {
        this.expireTime = LocalDateTime.now().plusSeconds(60);
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
        SmsCode smsCode = new SmsCode(code);
        SmsUtils.SendVerificationCodeBySms("13290981965", smsCode.getCode());

    }
}
