package com.rrong777.utils.code.sms;

/**
 * 短信发送者
 */
public interface SmsCodeSender {
    void send(String mobile, String code);
}
