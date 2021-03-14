package com.rrong777.validate.code;

import com.rrong777.utils.sms.SmsUtils;
import com.rrong777.utils.sms.VerificationCodeUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author wuqr
 * @Title: 短信验证码实体类
 * @Description: 封装短信验证码信息
 * @date 2020/4/19 13:51
 * 但是图片验证码要继承这个类，因为图片验证码只是多了一个图片属性，图片验证码继承
 * smscode又不合适，所以改名叫ValidateCode
 *
 * 没有实现Serializable 的话 就是不可序列化的。 因为现在实际上session是放到redis去管理，生成图形验证码的时候是把验证码放到session里面去。
 * 后面请求来 再从session里面拿出来验。现在放到session里实际上就是放到redis里面，放到redis里面的东西都是要求可序列化的。
 */
public class ValidateCode implements Serializable {
    private static final long serialVersionUID = -436475944223625617L;
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
