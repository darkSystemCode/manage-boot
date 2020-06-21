package com.hjx.blog_backstage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@MapperScan("com.hjx.blog_backstage.Mappers")
@EnableConfigurationProperties
public class BlogBackstageApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogBackstageApplication.class, args);
    }

}
