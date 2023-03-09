package com.hjx.blog_backstage.Configs;

import com.hjx.blog_backstage.Utils.RedisSerializeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        //string序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        //fastJSon序列化
        RedisSerializeUtils redisSerialize = new RedisSerializeUtils(Object.class);
        //key采用String的序列化方式
        redisTemplate.setKeySerializer(stringRedisSerializer);
        //hash key使用String序列化方式
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        //value使用fastJson序列化方式
        redisTemplate.setValueSerializer(redisSerialize);
        //hash的value使用fastJson的序列化方式
        redisTemplate.setHashValueSerializer(redisSerialize);
        redisTemplate.setDefaultSerializer(stringRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
