package com.hjx.blog_backstage.Controllers;

import com.hjx.blog_backstage.Entitys.Mail;
import com.hjx.blog_backstage.Services.mailService;
import com.hjx.blog_backstage.Utils.Result;
import com.hjx.blog_backstage.Utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class mailController {
    private static ResultUtil result = new ResultUtil();
    @Autowired
    private mailService mailService;

    @RequestMapping(value = "/sendMail", method = RequestMethod.POST)
    public Result sendMail(@RequestBody Mail mail) {
        if (mail != null) {
            Integer integer = mailService.sendMail(mail);
            if (integer != 0) {
                return result.success();
            }
        }
        return result.fail();
    }


    @RequestMapping(value = "/getEmailAdv", method = RequestMethod.GET, produces = "application/json")
    public Result getEmailAdvise(Integer currPage) {
        List<Mail> advise = mailService.getAdvise(currPage);
        if (advise != null) {
            return result.success(advise);
        }
        return result.fail();
    }

    /**
     * @params mail:mail实体类
     * 需要设置发件人
     * 收件人（前端给）
     * 发件主题
     * 发件内容（前端给）
     * @since 2020-04-06
     * 使用springboot集成的邮件功能发送邮件
     */
    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
    public Result sendEmail(@RequestBody Mail mail) {
        try {
            Integer resultCode = mailService.replyMail(mail.getSendEmail(), mail.getAdvises());
            if(resultCode == 200) {
                Integer integer = mailService.updateState(mail.getSendEmail());
                List<Mail> advise = mailService.getAdvise(1);
                if(integer != 0 && advise != null) {
                    return result.success(advise);
                }
            } else {
                return result.fail();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.fail();
    }
}
