package com.hjx.blog_backstage.Services;

import com.hjx.blog_backstage.Entitys.User;
import com.hjx.blog_backstage.Entitys.UserLogin;
import com.hjx.blog_backstage.Jwt.JwtToken;
import com.hjx.blog_backstage.Mappers.UserMapper;
import com.hjx.blog_backstage.Utils.JwtUtil;
import com.hjx.blog_backstage.Utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class LoginService {
    @Autowired
    private UserMapper userMapper;

    public Map<String, Object> login(User user) {
        HashMap<String, Object> result = new HashMap<>();
        //获取shiro的实体对象
        Subject subject = SecurityUtils.getSubject();
        //查找数据库中的用户数据
        User userResult = userMapper.findUser(user.getUsername(), user.getPassword());
        if(StringUtils.isEmpty(userResult.getAutoLogin())) {
            Integer integer = userMapper.updateUser(user.getAutoLogin(), userResult.getUserID());
            if(integer > 0) {
                userResult.setAutoLogin(user.getAutoLogin());
            }
        } else {
            if(userResult.getAutoLogin().equals(user.getAutoLogin())) {
                Integer integer = userMapper.updateUser(user.getAutoLogin(), userResult.getUserID());
                if(integer > 0) {
                    userResult.setAutoLogin(user.getAutoLogin());
                }
            }
        }
        if(!StringUtils.isEmpty(userResult) && !subject.isAuthenticated()) {
            //生成token
            String token = JwtUtil.sign(userResult.getUserID());
            JwtToken jwtToken = new JwtToken(token);
            try {
                //登入
                subject.login(jwtToken);
                Integer integer = userMapper.loginToken(userResult.getUserID(), token);
                if(integer > 0) {
                    result.put("token", token);
                    result.put("user", userResult);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            //如果用户名和密码查询为空 则只使用用户名查询 为空则未知账号错误 否则密码错误
            User userRes = userMapper.findUser(user.getUsername(), null);
            if(StringUtils.isEmpty(userRes)) {
                log.info("错误信息，未知用户名！");
                throw new UnknownAccountException("未知账号！");
            } else {
                log.info("错误信息，密码错误！");
                throw new IncorrectCredentialsException("密码错误！");
            }
        }
        return result;
    }

    public User autoLogin(String user_id) {
        return userMapper.getLoginData(user_id);
    }

    public User findUserByUid(String uid) {
        return userMapper.findUserByUid(uid);
    }

    public UserLogin checkLogin(String uid) {
        return userMapper.checkLogin(uid);
    }

    public Integer updateToken(String newToken, String user_id) {
        return userMapper.updateToken(newToken, user_id);
    }

}
