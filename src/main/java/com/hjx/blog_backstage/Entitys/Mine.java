package com.hjx.blog_backstage.Entitys;

public class Mine {
    private Integer mid; //ID
    private String author; //作者
    private String skill; //拥有技能
    private String introduce; //个人简介
    private String avatar; //个人头像
    private String backgAvatar; //背景图
    /*文章、评论、点赞、阅读总数*/
    private Integer articleTotal;
    private Integer discussTotal;
    private Integer likeTotal;
    private Integer readTotal;

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBackgAvatar() {
        return backgAvatar;
    }

    public void setBackgAvatar(String backgAvatar) {
        this.backgAvatar = backgAvatar;
    }

    public Integer getArticleTotal() {
        return articleTotal;
    }

    public void setArticleTotal(Integer articleTotal) {
        this.articleTotal = articleTotal;
    }

    public Integer getDiscussTotal() {
        return discussTotal;
    }

    public void setDiscussTotal(Integer discussTotal) {
        this.discussTotal = discussTotal;
    }

    public Integer getLikeTotal() {
        return likeTotal;
    }

    public void setLikeTotal(Integer likeTotal) {
        this.likeTotal = likeTotal;
    }

    public Integer getReadTotal() {
        return readTotal;
    }

    public void setReadTotal(Integer readTotal) {
        this.readTotal = readTotal;
    }
}
