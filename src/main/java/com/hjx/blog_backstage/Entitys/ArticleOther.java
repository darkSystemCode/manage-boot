package com.hjx.blog_backstage.Entitys;

public class ArticleOther {
    private Integer aoid; //id
    private String articleNum; //文章序号
    private String readingNum; //阅读量
    private String likeNum; //点赞量

    public Integer getAoid() {
        return aoid;
    }

    public void setAoid(Integer aoid) {
        this.aoid = aoid;
    }

    public String getArticleNum() {
        return articleNum;
    }

    public void setArticleNum(String articleNum) {
        this.articleNum = articleNum;
    }

    public String getReadingNum() {
        return readingNum;
    }

    public void setReadingNum(String readingNum) {
        this.readingNum = readingNum;
    }

    public String getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(String likeNum) {
        this.likeNum = likeNum;
    }
}
