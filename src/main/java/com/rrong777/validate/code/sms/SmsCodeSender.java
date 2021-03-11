package com.rrong777.validate.code.sms;

/**
 * 短信发送者
 */
public interface SmsCodeSender {
    /**
     * 发送方法
     * @param mobile 手机号
     * @param code 验证码
     */
    void send(String mobile, String code);
}
