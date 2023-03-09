package com.hjx.blog_backstage.Utils;

public class Result<T> {
    private Integer code; //返回的状态码
    private String msg; //返回的消息
    private String type; //返回数据类型：成功，失败，警告，异常
    private T data; //返回的数据

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(Integer code, String msg, String type) {
        this.code = code;
        this.msg = msg;
        this.type = type;
    }

    public Result(Integer code, String msg, String type, T data) {
        this.code = code;
        this.msg = msg;
        this.type = type;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
