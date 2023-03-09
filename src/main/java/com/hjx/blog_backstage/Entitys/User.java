package com.hjx.blog_backstage.Entitys;

import lombok.Data;

@Data
public class User {
    private Integer uid;
    private String userID; //用户ID
    private String username; //用户名
    private String password; //密码
    private String oldPassword; //旧密码
    private String email; //邮箱账号
    private Integer state; //账号状态
    private Long createTime; //创建时间
    private Long updateTime; //修改时间
    private String perms;
    private String roles;
    private String autoLogin; //自动登录

}
