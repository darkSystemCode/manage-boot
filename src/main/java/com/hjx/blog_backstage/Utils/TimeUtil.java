package com.hjx.blog_backstage.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @date 2020-01-23
 * @author hjx
 * @desc 时间转换工具类
 */
public class TimeUtil {

    public static String transTime(Date date) {
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        return format;
    }
}
