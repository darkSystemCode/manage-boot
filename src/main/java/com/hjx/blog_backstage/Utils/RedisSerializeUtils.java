package com.hjx.blog_backstage.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * @date 2020-09-10
 * @author hjx
 * @desc fastJson反序列化redis数据 static代码块开启autoType白名单 把实体类加入白名单中
 * */
public class RedisSerializeUtils<T> implements RedisSerializer<T> {
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    static {
        ParserConfig.getGlobalInstance().addAccept("com.hjx.yanmall.entitys");
    }

    private Class<T> clazz;

    public RedisSerializeUtils(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }

    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);

        return (T) JSON.parseObject(str, clazz);
    }
}
