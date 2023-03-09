package com.hjx.blog_backstage.Entitys;

public class UserLogin {
    private String user_id; //主键、外键 用户的ID
    private String token; //token值

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
