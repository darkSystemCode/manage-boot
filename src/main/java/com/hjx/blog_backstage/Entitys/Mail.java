package com.hjx.blog_backstage.Entitys;

public class Mail {
    private Integer mid; //id
    private String sendAccount; //留言人QQ账号
    private String sendEmail; //留言人邮箱
    private String advises; //留言人提出建议
    private String date; //留言时间
    private String isReply; //是否已回复 0未回复 1已回复

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getSendAccount() {
        return sendAccount;
    }

    public void setSendAccount(String sendAccount) {
        this.sendAccount = sendAccount;
    }

    public String getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(String sendEmail) {
        this.sendEmail = sendEmail;
    }

    public String getAdvises() {
        return advises;
    }

    public void setAdvises(String advises) {
        this.advises = advises;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIsReply() {
        return isReply;
    }

    public void setIsReply(String isReply) {
        this.isReply = isReply;
    }
}
