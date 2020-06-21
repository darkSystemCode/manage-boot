package com.hjx.blog_backstage.Mappers;

import com.hjx.blog_backstage.Entitys.User;
import org.springframework.stereotype.Repository;

@Repository
public interface userMapper {

    public Integer registerUser(User user);

    public User toLogin(User user);

    public String checkOldPassW(String oldPassword);

    public Integer updatePassword(String newPassword, String username);
}
