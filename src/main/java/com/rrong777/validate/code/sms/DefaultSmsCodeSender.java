package com.rrong777.validate.code.sms;

public class DefaultSmsCodeSender implements SmsCodeSender{
    @Override
    public void send(String mobile, String code) {
        System.out.println("像手机：" + mobile + "发送验证码：" + code);
    }
}
