package com.rrong777.dto;

import com.fasterxml.jackson.annotation.JsonView;

public class User {
    public interface UserSimpleView{};
    public interface UserDetailView extends UserSimpleView {};
    private String username;
    private String password;

    @JsonView(UserSimpleView.class)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // 指定密码在详细视图展示，但是详细视图继承自简单视图，展示详细视图的时候会把简单视图的属性一起展示了
    @JsonView(UserDetailView.class)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
