package com.rrong777.web.properties;

public class SmsCodeProperties {
    // 验证码位数
    private int length = 6;
    // 验证码过期时间
    private int expireIn = 60;
    // 要处理的url
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
