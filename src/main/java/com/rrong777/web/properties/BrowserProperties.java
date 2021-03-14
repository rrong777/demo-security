package com.rrong777.web.properties;

public class BrowserProperties {
    // 这里给出一个默认值，如果有配置的话就用配置覆盖这个，如果没有配置的话，就用下面这个默认值
    private String loginPage = SecurityConstants.DEFAULT_LOGIN_PAGE_URL;
    // 记住我的功能token多久过期
    private int remembermeSeconds = 3600;
    // 退出url 默认是空的，没有配置的话就是没有提供退出的url
    private String signOutUrl;
    // 默认让他返回json
    private LoginType loginType = LoginType.JSON;
    private SessionProperties session = new SessionProperties();
    public void setSession(SessionProperties session) {
        this.session = session;
    }

    public String getSignOutUrl() {
        return signOutUrl;
    }

    public void setSignOutUrl(String signOutUrl) {
        this.signOutUrl = signOutUrl;
    }

    public SessionProperties getSession() {
        return session;
    }
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
