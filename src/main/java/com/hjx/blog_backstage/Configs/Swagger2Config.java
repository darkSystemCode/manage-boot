package com.hjx.blog_backstage.Configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author hjx
 * @date 2020-11-11
 * @desc Swagger配置类
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

   @Bean
    public Docket createRestApi() {
       return new Docket(DocumentationType.SWAGGER_2)
               .apiInfo(apiInfo())
               .enable(true)
               .select()
               //扫描包路径 扫描@Api
               .apis(RequestHandlerSelectors.basePackage("com.hjx.blog_backstage.Controllers"))
               //指定路径处理PathSelectors.any() 代表所有路径
               .paths(PathSelectors.any())
               .build();
   }

   private ApiInfo apiInfo() {
       return new ApiInfoBuilder()
               //设置文档标题
               .title("个人博客后台管理系统接口文档")
               //文档描述
               .description("接口说明")
               //版本号
               .version("1.0.0")
               .build();
   }
}
