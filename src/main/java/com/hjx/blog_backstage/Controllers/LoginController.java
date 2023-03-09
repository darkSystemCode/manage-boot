package com.hjx.blog_backstage.Controllers;

import com.hjx.blog_backstage.Entitys.User;
import com.hjx.blog_backstage.Jwt.JwtToken;
import com.hjx.blog_backstage.Services.LoginService;
import com.hjx.blog_backstage.Services.UserService;
import com.hjx.blog_backstage.Utils.JwtUtil;
import com.hjx.blog_backstage.Utils.Result;
import com.hjx.blog_backstage.Utils.ResultUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @date 2021-02-15
 * @author hjx
 * @desc 登录控制器
 */
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    /*
     * 登录，校验用户信息接口
     * user
     *  username
     *  password
     * return  token
     * */
    @PostMapping(value = "/Login", produces = "application/json")
    public Result Login(@RequestBody User user) {
        if (user.getUsername() != null && user.getPassword() != null) {
            Map<String, Object> login = loginService.login(user);
            if(login.size() > 0) {
                return ResultUtil.success(login);
            }
        }
        return ResultUtil.fail();
    }

    @PostMapping(value = "/getLoginData/{user_id}", produces = "application/json")
    public Result GetLoginData(@PathVariable("user_id") String user_id) {
        if(!StringUtils.isEmpty(user_id)) {
            User user = loginService.autoLogin(user_id);
            if(!StringUtils.isEmpty(user)) {
                return ResultUtil.success(user);
            }
        }
        return ResultUtil.fail();
    }

    /**
     * 登出
     */
    @GetMapping("/logout")
    public Result logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ResultUtil.success();
    }
}
