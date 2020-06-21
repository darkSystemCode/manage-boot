package com.hjx.blog_backstage.Mappers;

import com.hjx.blog_backstage.Entitys.Mail;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface mailMapper {

    public Integer sendMail(Mail mail);

    public List<Mail> getAdvise(Integer size);

    public Integer updateState(String sendEmail);
}
