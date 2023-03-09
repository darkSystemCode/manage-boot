package com.hjx.blog_backstage.Configs;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @date 2020-09-09
 * @author hjx
 * @desc 把默认的Jackjson改为fastjson处理json数据
 * */
@Configuration
public class MyConfigs implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setCharset(Charset.forName("UTF-8"));
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.WriteClassName,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteMapNullValue
        );
        //解决中文乱码问题
        ArrayList<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON);

        fastJsonHttpMessageConverter.setSupportedMediaTypes(mediaTypes);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(0, fastJsonHttpMessageConverter);
    }

    /**
     *
     * @param registry
     * @desc 解决前后端分离的请求跨域CORS问题 使用springboot的WebMvcConfigurer
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //匹配所有路径
                .allowedHeaders("*")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowCredentials(true)
                .maxAge(1800); //侦探冷却时间
    }
}
