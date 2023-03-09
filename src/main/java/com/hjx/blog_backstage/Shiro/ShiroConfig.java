package com.hjx.blog_backstage.Shiro;

import com.hjx.blog_backstage.Jwt.JwtFilter;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;

/**
 * @date 2021-02-14
 * @author hjx
 * @desc Shiro配置器
 */
@Configuration
public class ShiroConfig {

    //自定义shiro对象
    @Bean("shiroRealm")
    public MyRealm shiroRealm() {
        return new MyRealm();
    }

    //安全器
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //配置注入自定义realm
        securityManager.setRealm(shiroRealm());
        //关闭shiro自带session
        DefaultSubjectDAO defaultSubjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        defaultSubjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(defaultSubjectDAO);
        return securityManager;
    }

    //配置拦截路径
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        //自定义jwt拦截器
        LinkedHashMap<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("jwt", new JwtFilter());
        filterFactoryBean.setFilters(filterMap);
        filterFactoryBean.setSecurityManager(securityManager());

        //设置无权限时跳转的路径
//        filterFactoryBean.setLoginUrl("/Login");
//        filterFactoryBean.setSuccessUrl("/Welcome");
        filterFactoryBean.setUnauthorizedUrl("/filterError/**");

        //自定义拦截路径
        LinkedHashMap<String, String> resultMap = new LinkedHashMap<>();
        resultMap.put("/Login", "anon");
        resultMap.put("/logout", "anon");
        resultMap.put("/swagger-ui/", "anon");
        resultMap.put("/swagger-ui.html", "anon");
        resultMap.put("/swagger-resources", "anon");
        resultMap.put("/v2/api-docs", "anon");
        resultMap.put("/webjars/springfox-swagger-ui/**", "anon");
        resultMap.put("/filterError/**", "anon");
        resultMap.put("/**", "jwt");
        filterFactoryBean.setFilterChainDefinitionMap(resultMap);
        return filterFactoryBean;
    }

    //代理
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    //对Shiro注解的支持
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    //生命周期
    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

}
