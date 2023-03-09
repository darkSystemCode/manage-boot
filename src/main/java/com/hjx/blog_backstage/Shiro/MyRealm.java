package com.hjx.blog_backstage.Shiro;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.hjx.blog_backstage.Entitys.User;
import com.hjx.blog_backstage.Jwt.JwtToken;
import com.hjx.blog_backstage.Services.LoginService;
import com.hjx.blog_backstage.Services.UserService;
import com.hjx.blog_backstage.Utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author hjx
 * @date 2020-02-14
 * @desc 自定义Realm 对账号进行授权、认证
 */
@Slf4j
@Component
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private LoginService loginService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("正在授权中...");
        String uid = null;
        try {
            uid = JwtUtil.getUid(principalCollection.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        User user = loginService.findUserByUid(uid);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        try {
            simpleAuthorizationInfo.addStringPermission(user.getPerms());
            simpleAuthorizationInfo.addRole(user.getRoles());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return simpleAuthorizationInfo;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        String token = (String) authenticationToken.getPrincipal();
        String uid = JwtUtil.getUid(token);
        User user = loginService.findUserByUid(uid);
        if(!StringUtils.isEmpty(user)) {
            log.info("正在认证用户-{}-的信息...", user.getUsername());
            if (!JwtUtil.verify(token, uid)) {
                log.info("token 认证失败！");
                throw new AuthenticationException("token 认证失败！");
            }
        } else {
            throw new AuthenticationException("认证失败，无此用户！");
        }
        return new SimpleAuthenticationInfo(token, token, getName());
    }
}
