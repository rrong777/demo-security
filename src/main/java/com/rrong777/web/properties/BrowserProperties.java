package com.rrong777.web.properties;

public class BrowserProperties {
    // 这里给出一个默认值，如果有配置的话就用配置覆盖这个，如果没有配置的话，就用下面这个默认值
    private String loginPage = "/rrong777-signIn.html";
    // 记住我的功能token多久过期
    private int remembermeSeconds = 3600;
    // 默认让他返回json
    private LoginType loginType = LoginType.JSON;

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    public int getRemembermeSeconds() {
        return remembermeSeconds;
    }

    public void setRemembermeSeconds(int remembermeSeconds) {
        this.remembermeSeconds = remembermeSeconds;
    }
}
