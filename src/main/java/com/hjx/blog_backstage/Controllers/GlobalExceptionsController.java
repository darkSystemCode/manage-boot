package com.hjx.blog_backstage.Controllers;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.hjx.blog_backstage.Utils.Result;
import com.hjx.blog_backstage.Utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @date 2021-02-16
 * @author hjx
 * @desc 全局异常的处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionsController {

    @ExceptionHandler(RuntimeException.class)
    public Result RuntimeException(RuntimeException re) {
        log.error("2007 错误内容{}", re);
        return ResultUtil.exception(20007, "运行时异常");
    }

    @ExceptionHandler(UnauthorizedException.class)
    public Result UnauthorizedException(UnauthorizedException ue) {
        log.error("20008,错误内容{}", ue);
        return ResultUtil.exception(20008, "权限不足！");
    }

    @ExceptionHandler(AuthorizationException.class)
    public Result AuthorizationException(AuthorizationException ae) {
        log.error("20008,错误内容{}", ae);
        return ResultUtil.exception(20009, "权限不足！");
    }

    @ExceptionHandler(TokenExpiredException.class)
    public Result TokenExpiredException(TokenExpiredException te) {
        log.error("20010 错误内容{}", te);
        return ResultUtil.exception(20010, te.getMessage());
    }

    @ExceptionHandler(UnknownAccountException.class)
    public Result UnknownAccountException(UnknownAccountException ue) {
        log.error("20011 错误内容{}", ue);
        return ResultUtil.exception(20011, ue.getMessage());
    }

    @ExceptionHandler(IncorrectCredentialsException.class)
    public Result IncorrectCredentialsException(IncorrectCredentialsException ie) {
        log.error("20012 错误内容{}", ie);
        return ResultUtil.exception(20012, ie.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result MissingServletRequestParameterException(MissingServletRequestParameterException miisE) {
        log.error("400 错误内容{}", miisE);
        return ResultUtil.exception(400, miisE.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result Exception(Exception e) {
        log.error("500 错误内容{}", e);
        return ResultUtil.exception(500, e.getMessage());
    }

}
