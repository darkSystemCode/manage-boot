package com.hjx.blog_backstage.Utils;

import javax.servlet.http.Cookie;

/**
 * @author hjx
 * @date 2020-05-16
 * cookie工具类
 * */
public class CookieUtil {

    public static String getCookie(Cookie[] cookies, String targetV) {
        if(cookies != null) {
            for (Cookie item: cookies) {
                if(item.getName().equalsIgnoreCase(targetV)) {
                    return item.getValue();
                }
            }
        } else {
            return null;
        }
        return null;
    }
}
