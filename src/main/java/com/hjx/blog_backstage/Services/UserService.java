package com.hjx.blog_backstage.Services;

import com.hjx.blog_backstage.Entitys.User;
import com.hjx.blog_backstage.Mappers.userMapper;
import com.hjx.blog_backstage.Utils.MailUtil;
import com.hjx.blog_backstage.Utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Calendar;

@Service
public class UserService {

    @Autowired
    private userMapper userMapper;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String email;

    private HttpSession session = null;

    /*
     * 发送验证码到验证邮箱中， 并存入session（2分钟有效）
     * */
    public Integer setCheckCode(String toEmail, HttpServletRequest request) {
        String checkCode = UUIDUtil.getCheckCode();
        session = request.getSession(true);
        session.setAttribute("code", checkCode);
        session.setMaxInactiveInterval(60 * 2); //2分钟失效
        SimpleMailMessage message = MailUtil.sendCheckCode(email, toEmail, checkCode);
        try {
            javaMailSender.send(message);
            return 200;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /*
     * 前台发送请求校验验证码是否正确，正确返回200
     * */
    public Integer getCheckCode(String code, HttpServletRequest request) {
        String checkCode = (String) session.getAttribute("code");
        if (code.equalsIgnoreCase(checkCode)) {
            //校验通过,清空session缓存
            session.setAttribute("code", "");
            return 200;
        }
        return 0;
    }

    public Integer registerUser(User user) {
        Calendar date = Calendar.getInstance();
        user.setUserID(UUIDUtil.get_64UUID());
        user.setUsername(user.getUsername());
        user.setPassword(user.getPassword());
        user.setEmail(user.getEmail());
        user.setCreateTime(date.getTimeInMillis());
        user.setState(1);
        return userMapper.registerUser(user);
    }

    public User toLogin(User user) {
        return userMapper.toLogin(user);
    }

    public Integer checkOldPassW(String oldPassword) {
        String checkRes = userMapper.checkOldPassW(oldPassword);
        if(checkRes == null || checkRes == "") {
            return 201;
        }
        return 0;
    }

    public Integer updatePassword(String newPassword, String username) {
        return userMapper.updatePassword(newPassword, username);
    }
}
