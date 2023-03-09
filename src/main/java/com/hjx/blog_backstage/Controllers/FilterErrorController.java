package com.hjx.blog_backstage.Controllers;

import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @date 2021-02-18
 * @author hjx
 * @desc shiro+jwt异常的处理 异常继续抛给全局异常GlobalExceptionsController处理
 */
@RestController
public class FilterErrorController {

    @RequestMapping("/filterError/{msg}")
    public void filterError(@PathVariable("msg") String msg) {
        throw new TokenExpiredException(msg);
    }
}
