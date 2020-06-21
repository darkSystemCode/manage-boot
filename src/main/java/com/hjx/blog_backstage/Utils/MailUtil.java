package com.hjx.blog_backstage.Utils;

import org.springframework.mail.SimpleMailMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 发送邮件工具类
 * @author hjx
 * @since 2020-05-03
 * */
public class MailUtil {
    private static SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

    public static SimpleMailMessage sendEmail(String from, String to, String text) {
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject("博主的答复，请签收");
        simpleMailMessage.setText(text);
        return simpleMailMessage;
    }

    public static SimpleMailMessage sendCheckCode(String from, String to, String code) {
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject("注册账号激活码");
        simpleMailMessage.setText("激活码为"+code);
        return simpleMailMessage;
    }
}
