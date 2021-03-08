package com.rrong777.utils.code.sms;

public class DefaultSmsSender implements SmsCodeSender{
    @Override
    public void send(String mobile, String code) {
        System.out.println("像手机：" + mobile + "发送验证码：" + code);
    }
}
