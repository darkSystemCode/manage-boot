package com.hjx.blog_backstage.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hjx.blog_backstage.Mappers.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * @author hjx
 * @date 2020.11.12
 */
@Component
@Slf4j
public class JwtUtil {

    private static UserMapper userMapper;
    private static long EXPIRE_TIME;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        JwtUtil.userMapper = userMapper;
    }

    @Value("${jwt.token_expired}")
    public void setExpireTime(long expireTime) {
        EXPIRE_TIME = expireTime;
    }
    /*
     * JWT验证过期时间/分钟
     * */
//    private static final long EXPIRE_TIME = 60 * 1000;

    /**
     * 校验token
     */
    public static boolean verify(String token, String secret) {
        try {
            //根据密码生成JWT校验器
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("uid", secret)
                    .build();
            //校验token
            verifier.verify(token);
            log.info("token校验成功");
            return true;
        } catch (JWTVerificationException jwte) {
            log.error("无效签名", jwte);
            return false;
        } catch (Exception e) {
            System.out.println("token校验失败");
            System.out.println(e);
            return false;
        }
    }

    /**
     * 通过秘钥名获得token的秘钥值 如果出现异常返回null
     */
    public static String getUid(String token) {
        try {
            DecodedJWT decode = JWT.decode(token);
            return decode.getClaim("uid").asString();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 生成token签名
     * 1.EXPIRE_TIME分钟后过期
     * 2.设置uid为秘钥
     */
    public static String sign(String uid) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(uid);
        return JWT.create()
                .withClaim("uid", uid)
                .withExpiresAt(date)
                .sign(algorithm);
    }

    /**
     * 检查token的过期时间
     *
     * @param token 前端发送来的token信息
     * @return 返回true token过期 返回false token未过期
     */
    public static boolean checkTokenTime(String token) {
        Calendar calendar = Calendar.getInstance();
        DecodedJWT decode = JWT.decode(token);
        Date expiresAt = decode.getExpiresAt();
        //isExpired返回true 当前token已过期 否则token未过期
        boolean isExpired = expiresAt.before(calendar.getTime());
        if(isExpired) {
            log.info("token已过期，过期时间{}", TimeUtil.transTime(expiresAt));
            //token已过期 自动更新token
            return false;
//            return updateToken(decode.getClaim("uid").asString());
        }
        return true;
        /*DecodedJWT decode = JWT.decode(token);
        Calendar calendar = Calendar.getInstance();
        //得到token的过期时间 如果过期时间早于当前的时间 则token已过期
        Date expiresAt = decode.getExpiresAt();
        String uid = decode.getClaim("uid").asString();
        UserLogin userLogin = loginService.checkLogin(uid);
        log.info("token到期时间为{}", TimeUtil.transTime(expiresAt));
        //递归退出条件
        if(isExpired) {
            String newToken = sign(uid);
            //把新的token保存到数据库中
            try {
                Integer integer = loginService.updateToken(newToken, userLogin.getUser_id());
                if(integer > 0) {
                    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
                    httpServletRequest.setAttribute("token", newToken);
                    return isExpired;
                }
            } catch (Exception e) {
                throw new AuthenticationException("存储token失败！");
            }
        }
        isExpired = expiresAt.before(calendar.getTime());
        //递归校验服务器中保存的token当前是否是登录状态
        // 如果当前是登录状态则出错返回给前端 让前端跳转登录页面重新登录 否则自动更新token继续登录
        if(number == 2 && isExpired == false) {
            number = 0;
            isExpired = true;
            return isExpired;
        }
        number++;
        checkTokenTime(userLogin.getToken(), request);
        return isExpired;*/
    }

    /**
     * 更新当前用户的token
     * @param uid
     * @return true 更新成功 false 更新失败
     */
    private static final String updateToken(String uid) {
        String token = JwtUtil.sign(uid);
        try {
            Integer integer = userMapper.loginToken(uid, token);
            if(integer > 0) {
                return token;
            }
        } catch (Exception e) {
            log.error("错误内容 {}", e.getMessage());
            return null;
        }
        return null;
    }


    public static void main(String[] args) {
        /**
         * 测试生成一个token
         */
        //13dfa22ce4ae4b2e92a32acb7628db71
        String sign = sign("13dfa22ce4ae4b2e92a32acb7628db71");
        System.out.println("测试生成一个token\n" + sign);
    }
}
