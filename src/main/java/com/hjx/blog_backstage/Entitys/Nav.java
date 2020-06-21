package com.hjx.blog_backstage.Entitys;

import java.util.List;

public class Nav{
    private Integer nid; //id
    private String title; //导航标题
    private String path; //导航路径
    private List<ChildNav> children; //子导航

    public Integer getNid() {
        return nid;
    }

    public void setNid(Integer nid) {
        this.nid = nid;
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

    public List<ChildNav> getChildren() {
        return children;
    }

    public void setChildren(List<ChildNav> children) {
        this.children = children;
    }
}
