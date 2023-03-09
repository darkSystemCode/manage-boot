package com.hjx.blog_backstage.Entitys;

public class ChildNav {
    private Integer ncid; //id
    private Integer navNumber; //父级导航索引
    private String title; //导航标题
    private String path; //导航路径
    private String navDesc; //导航描述

    public Integer getNcid() {
        return ncid;
    }

    public void setNcid(Integer ncid) {
        this.ncid = ncid;
    }

    public Integer getNavNumber() {
        return navNumber;
    }

    public void setNavNumber(Integer navNumber) {
        this.navNumber = navNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNavDesc() {
        return navDesc;
    }

    public void setNavDesc(String navDesc) {
        this.navDesc = navDesc;
    }
}
