package com.hjx.blog_backstage.Utils;

import java.util.UUID;

/**
 * @since 2020-03-27
 * 随机生成64位随机数
 * 去除-
 * 64
 * 32
 */
public class UUIDUtil {

    public static String get_64UUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String get_32UUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "").substring(0, uuid.length()/2);
    }

    public static String getCheckCode() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "").substring(0, uuid.length()/8);
    }
}
