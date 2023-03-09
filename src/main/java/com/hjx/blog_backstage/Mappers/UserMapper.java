package com.hjx.blog_backstage.Mappers;

import com.hjx.blog_backstage.Entitys.Perms;
import com.hjx.blog_backstage.Entitys.Roles;
import com.hjx.blog_backstage.Entitys.User;
import com.hjx.blog_backstage.Entitys.UserLogin;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    public User getUserID(String userID);

    public List<User> getUser(Integer page, Integer pageSize);

    public Integer getUserTotal();

    public User findUser(String username, String password);

    public User findUserByUid(String uid);

    public UserLogin checkLogin(String uid);

    public Integer updateToken(String newToken, String user_id);

    public Integer loginToken(String uid, String token);

    public Integer registerUser(User user);

    public User toLogin(User user);

    public String checkOldPassW(String oldPassword);

    public Integer updatePassword(String newPassword, String username);

    public Integer editUser(User user);

    Integer delUser(@Param("user_id") String user_id);

    void loginToken(String token);

    public Integer updateUser(String autoLogin, String userID);

    public User getLoginData(String user_id);

    public Integer setUserPerms(Perms perms);

    public Integer setUserRoles(Roles roles);

    public List<Perms> getPerms();

    public List<Roles> getRoles();

    public Integer addAccount(User user);

    public User checkEmail(String email);

    public List<Perms> getAllPerms(Integer page, Integer pageSize);

    public List<Roles> getAllRoles(Integer page, Integer pageSize);

    public Integer getPermsTotal();

    public Integer getRolesTotal();

    public Integer delPerms(String permsName);

    public Integer delRoles(String rolesName);
}
