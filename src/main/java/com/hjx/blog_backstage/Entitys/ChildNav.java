package com.hjx.blog_backstage.Entitys;

public class ChildNav {
    private Integer ncid; //id
    private Integer navNumber; //父级导航索引
    private String childTitle; //导航标题
    private String childPath; //导航路径

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

    public String getChildTitle() {
        return childTitle;
    }

    public void setChildTitle(String childTitle) {
        this.childTitle = childTitle;
    }

    public String getChildPath() {
        return childPath;
    }

    public void setChildPath(String childPath) {
        this.childPath = childPath;
    }
}
