package com.hjx.blog_backstage.Services;

import com.hjx.blog_backstage.Entitys.Mail;
import com.hjx.blog_backstage.Mappers.mailMapper;
import com.hjx.blog_backstage.Utils.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Service
public class mailService {
    @Autowired
    private mailMapper mailMapper;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String email;

    public Integer sendMail(Mail mail) {
        long currMillis = Calendar.getInstance().getTimeInMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(currMillis);
        mail.setDate(format);
        mail.setIsReply("0");
        return mailMapper.sendMail(mail);
    }

    public List<Mail> getAdvise(Integer currPage) {
        Integer size;
        if(currPage == 0 || currPage == 1) {
            size = 0;
        } else {
            size = currPage * 10;
        }
        return mailMapper.getAdvise(size);
    }

    public Integer replyMail(String toEmail, String text) {
        SimpleMailMessage simpleMailMessage = MailUtil.sendEmail(email, toEmail, text);
        try {
            javaMailSender.send(simpleMailMessage);
            return 200;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Integer updateState(String sendEmail) {
        return mailMapper.updateState(sendEmail);
    }
}
