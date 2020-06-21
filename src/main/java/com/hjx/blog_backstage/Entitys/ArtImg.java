package com.hjx.blog_backstage.Entitys;

public class ArtImg {
    private Integer aiid; //ID
    private String articleNum; //文章索引
    private String imgKey; //图片索引
    private String img;

    public Integer getAiid() {
        return aiid;
    }

    public void setAiid(Integer aiid) {
        this.aiid = aiid;
    }

    public String getArticleNum() {
        return articleNum;
    }

    public void setArticleNum(String articleNum) {
        this.articleNum = articleNum;
    }

    public String getImgKey() {
        return imgKey;
    }

    public void setImgKey(String imgKey) {
        this.imgKey = imgKey;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
