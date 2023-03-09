package com.hjx.blog_backstage.Entitys;

public class Roles {
    private Integer id;
    private String user_id;
    private String rolesName;
    private String rolesDesc;

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

    public String getRolesName() {
        return rolesName;
    }

    public void setRolesName(String rolesName) {
        this.rolesName = rolesName;
    }

    public String getRolesDesc() {
        return rolesDesc;
    }

    public void setRolesDesc(String rolesDesc) {
        this.rolesDesc = rolesDesc;
    }
}
