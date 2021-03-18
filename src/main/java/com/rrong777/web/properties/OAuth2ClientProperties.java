package com.rrong777.web.properties;

public class OAuth2ClientProperties {
    private String clientId;
    private String clientSecret;
    private int accessTokenValiditySeconds = 7200; // 这里给一个默认值，如果不写默认值的话默认就是0 ，如果这个过期时间是0 就是不会过期的。
    // 能配置的都可以声明在这里


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public int getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    public void setAccessTokenValiditySeconds(int accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }
}
