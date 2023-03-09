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
    public static Result success(Object data) {
        Result result = new Result(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), "success", data);
        return result;
    }

    public static Result success(String msg) {
        Result result = new Result(ResultEnum.SUCCESS.getCode(), msg, "success");
        return result;
    }

    public static Result success(String msg, Object data) {
        Result result = new Result(ResultEnum.SUCCESS.getCode(), msg, "success", data);
        return result;
    }

    /*
     * 请求成功，无数据返回
     * */
    public static Result success() {
        ResultUtil resultUtil = new ResultUtil();
        return resultUtil.success("");
    }

    /*
     * 自定义返回状态码
     * */
    public static Result success(Integer code, String msg) {
        return new Result(code, msg);
    }

    public static Result success(Integer code, String msg, Object data) {
        return new Result(code, msg, data);
    }

    /*
     * 请求失败，返回数据为空
     * */
    public static Result fail() {
        Result result = new Result(ResultEnum.FAIL.getCode(), ResultEnum.FAIL.getMsg(), "error");
        return result;
    }

    public static Result fail(String msg) {
        Result result = new Result(ResultEnum.FAIL.getCode(), msg, "error");
        return result;
    }

    /*
     * 请求失败，返回自定义信息
     * */
    public static Result fail(Integer code, String msg) {
        Result result = new Result(code, msg, "error");
        return result;
    }

    /**
     * 异常信息返回
     * @return result
     */

    public static Result exception(Integer code, String msg) {
        Result result = new Result(code, msg, "error");
        return result;
    }

    /*
     * 错误返回
     * */
    public static Result error(Object error) {
        Result result = new Result(ResultEnum.ERROR.getCode(), ResultEnum.ERROR.getMsg(), error);
        return result;
    }
}
