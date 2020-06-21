package com.hjx.blog_backstage.Configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author hjx
 * @since 2020-03-22
 * @remark 解决前后端分离的请求跨域CORS问题 使用springboot的WebMvcConfigurer
 */

@Configuration
public class CORSConfig {

    @Bean
    public WebMvcConfigurer corsConfig() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") //匹配所有的路径
                        .allowedHeaders("*")
                        .allowedOrigins("*")
                        .allowCredentials(true)
                        .allowedMethods("*") //匹配所有请求方式
                        .maxAge(30);
            }
        };
    }
}
