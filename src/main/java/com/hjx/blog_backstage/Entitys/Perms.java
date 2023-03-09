package com.hjx.blog_backstage.Entitys;

public class Perms {
    private Integer id;
    private String user_id;
    private String permsName;
    private String permsDesc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPermsName() {
        return permsName;
    }

    public void setPermsName(String permsName) {
        this.permsName = permsName;
    }

    public String getPermsDesc() {
        return permsDesc;
    }

    public void setPermsDesc(String permsDesc) {
        this.permsDesc = permsDesc;
    }
}
