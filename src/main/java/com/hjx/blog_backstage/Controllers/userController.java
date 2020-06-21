package com.hjx.blog_backstage.Controllers;

import com.hjx.blog_backstage.Entitys.User;
import com.hjx.blog_backstage.Services.UserService;
import com.hjx.blog_backstage.Utils.CookieUtil;
import com.hjx.blog_backstage.Utils.Result;
import com.hjx.blog_backstage.Utils.ResultUtil;
import com.hjx.blog_backstage.Utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 用户管理类，处理登录，注册，校验等
 */
@RestController
public class userController {
    private static ResultUtil result = new ResultUtil();
    @Autowired
    private UserService userService;

    @GetMapping(value = "/sendCheckCode", produces = "application/json")
    public Result getCheckCode(String email, HttpServletRequest request) {
        Integer checkCode = userService.setCheckCode(email, request);
        if (checkCode == 200) {
            return result.success();
        }
        return result.fail();
    }

    @RequestMapping(value = "/getCheckCode", method = RequestMethod.GET, produces = "application/json")
    public Result checkCode(String code, HttpServletRequest request) {
        Integer resultCode = userService.getCheckCode(code, request);
        if (resultCode == 200) {
            return result.success();
        }
        return result.fail();
    }

    /*
     * 注册账号
     * username
     * password
     * email
     * */
    @PostMapping(value = "/toRegister")
    public Result register(User user) {
        Integer integer = userService.registerUser(user);
        if (integer != 0) {
            return result.success();
        }
        return result.fail();
    }

    /*
     * 登录，校验用户信息接口
     * user
     *      username
     *      password
     * request:获得请求的cookie 如果得到指定的cookie，查询数据库，如果存在，则用户已登录，否则，未登录
     * response：返回登录成功的cookie信息
     * return  token
     * */
    @PostMapping(value = "/toLogin", produces = "application/json")
    public Result Login(User user, HttpServletRequest request, HttpServletResponse response) {
        if (user.getUsername() != null && user.getPassword() != null) {
            Cookie[] cookies = request.getCookies();
            String cookieValue = CookieUtil.getCookie(cookies, "user");
            //cookieValue 不为null？登录：未登录
            User userData = null;
            HttpSession session = request.getSession(true);
            if (cookieValue == null || cookieValue == "") { //未登录
                userData = userService.toLogin(user);
                if (userData != null) {
                    //返回本次登录成功的cookie信息
                    String sessionId = session.getId();
                    session.setAttribute("user", user);
                    session.setMaxInactiveInterval(60 * 60 * 24 * 15);//15天有效期
                    //返回cookie信息 key:user value: sessionID
                    Cookie cookie = new Cookie("user", sessionId);
                    response.addCookie(cookie);
                    String token = UUIDUtil.get_64UUID();
                    userData.setToken(token);
                    return result.success(userData);
                }
            } else if (session.getId().equals(cookieValue) == false) { //登陆过，但是已失效session
                return result.success(201, "登录认证已失效，请重新登录");
            } else { //已登录过 看用户是否勾选自动登录，如果勾选，则实现自动登录
                String sessionId = session.getId();
                if (sessionId.equals(cookieValue)) {
                    User sessionUser = (User) session.getAttribute("user");
                    User userRes = userService.toLogin(sessionUser);
                    if (userRes != null) {
                        //返回cookie信息 key:user value: sessionID
                        Cookie cookie = new Cookie("user", sessionId);
                        response.addCookie(cookie);
                        String token = UUIDUtil.get_64UUID();
                        userRes.setToken(token);
                        return result.success(userRes);
                    }
                }

            }
        }
        return result.fail();
    }

    /**
     * 修改密码
     */

    @RequestMapping(value = "/updatePassword/{username}", method = RequestMethod.POST, produces = "application/json")
    public Result updatePassword(@PathVariable(value = "username") String username,
                                 @RequestBody User user) {
        //新旧密码不能一致
        if(user.getPassword().equals(user.getOldPassword())) {
            return result.fail(201, "新旧密码不能一致！");
        } else {
            //校验旧密码是否正确
            Integer checkRes = userService.checkOldPassW(user.getOldPassword());
            if(checkRes == 201) {
                return result.fail(201, "旧密码不存在，无法修改密码！");
            } else if (checkRes == 0){
                //修改密码
                Integer integer = userService.updatePassword(user.getPassword(), username);
                if(integer != 0) {
                    return result.success(200, "密码修改成功！");
                } else {
                    return result.fail(201, "密码修改失败");
                }
            }
        }
        return result.fail(201, "密码修改失败");
    }
}
