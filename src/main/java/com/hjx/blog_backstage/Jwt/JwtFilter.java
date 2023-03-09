package com.hjx.blog_backstage.Jwt;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.hjx.blog_backstage.Utils.JwtUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * @author hjx
 * @date 2021-02-14
 * @desc JWT自定义拦截器 拦截登录 判断token
 */
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {
    /**
     * 过滤器最先执行的方法，调用isLoginAttempt()判断请求头是否有token
     * 如果有token，则执行executeLogin（）执行登录操作
     * 如果没有token，则返回true，表示首次登录或者游客身份进行访问
     * <p>
     * 如果执行executeLogin（）出现异常 转发到Controller的/filterError层 交由全局异常处理
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return true
     */
    @SneakyThrows
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
            try {
                executeLogin(request, response);
            } catch (AuthenticationException e) {
                log.info("异常错误：{}", e.getMessage());
                requestDispatcher(request, response, e.getMessage());
                //返回false 避免继续走校验token
                return false;
            }
        }
        // 不管是否存在token头都返回true
        return true;
    }

    /**
     * 校验header是否有token
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        return getTokenFromRequest(request) != null;
    }

    /**
     * 执行登录操作 成功返回true
     *
     * @param request
     * @param response
     * @return true
     * @throws Exception
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        boolean isExpired = JwtUtil.checkTokenTime(getTokenFromRequest(request));
        if(!isExpired) {
            requestDispatcher(request, response, "token已过期，请重新登录");
        }
        /*boolean isExpired = JwtUtil.checkTokenTime(getTokenFromRequest(request), request);
        if(isExpired) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            token = (String) httpServletRequest.getAttribute("token");
        } else {
            token = getTokenFromRequest(request);
        }*/
        //获取前端发送请求头的token
        String token = getTokenFromRequest(request);
        JwtToken jwtToken = new JwtToken(token);
        // 提交给 realm 进行登录,如果错误,会抛出异常
        getSubject(request, response).login(jwtToken);
        // 如果没有抛出异常,则代表登录成功,返回true
        return true;
    }

    /**
     * @param request
     * @return 从request从提取出token值
     */
    private String getTokenFromRequest(ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        System.out.println(httpServletRequest.getHeader("token"));
        return httpServletRequest.getHeader("token");
    }

    /**
     * 非法路径跳转
     * @param request
     * @param response
     * @param msg
     */
    @SneakyThrows
    private void requestDispatcher(ServletRequest request, ServletResponse response, String msg) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        httpServletRequest.getRequestDispatcher("/filterError/" + msg).forward(request, response);
    }

}
