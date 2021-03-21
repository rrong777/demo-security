package com.rrong777.web.properties;

public class OAuth2Properties {
    // 发出去的时候使用他签名，验他的时候也会使用他进行验签。需要保存好，别人知道可以用你的密钥去签发令牌。因为令牌唯一的安全就是在这个密钥，
    // 如果拿到密钥签发你的令牌就可以随意进入你的系统，
    private String jwtSigningKey = "rrong777";
    private OAuth2ClientProperties[] clients = {};

    public OAuth2ClientProperties[] getClients() {
        return clients;
    }

    public void setClients(OAuth2ClientProperties[] clients) {
        this.clients = clients;
    }

    public String getJwtSigningKey() {
        return jwtSigningKey;
    }

    public void setJwtSigningKey(String jwtSigningKey) {
        this.jwtSigningKey = jwtSigningKey;
    }
}
