package com.hjx.blog_backstage.Utils;

/**
 * @author hjx
 * @since 2020-05-02
 * 统一返回结果工具类
 */
public class ResultUtil {

    /*
     * 请求成功，并返回数据
     * */
    public Result success(Object data) {
        Result result = new Result(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), data);
        return result;
    }

    /*
     * 请求成功，无数据返回
     * */
    public Result success() {
        ResultUtil resultUtil = new ResultUtil();
        return resultUtil.success("");
    }

    /*
     * 自定义返回状态码
     * */
    public Result success(Integer code, String msg) {
        return new Result(code, msg);
    }

    /*
     * 请求失败，返回数据为空
     * */
    public Result fail() {
        Result result = new Result(ResultEnum.FAIL.getCode(), ResultEnum.FAIL.getMsg(), "");
        return result;
    }

    /*
     * 请求失败，返回自定义信息
     * */
    public Result fail(Integer code, String msg) {
        Result result = new Result(code, msg);
        return result;
    }

    /*
     * 异常错误返回
     * */
    public Result error(Object error) {
        Result result = new Result(ResultEnum.ERROR.getCode(), ResultEnum.ERROR.getMsg(), error);
        return result;
    }
}
