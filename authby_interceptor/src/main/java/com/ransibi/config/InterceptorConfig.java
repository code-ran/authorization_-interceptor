package com.ransibi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description:
 * @author: rsb
 * @description: 2023-11-15-11-29
 * @description:
 * @Version: 1.0.0
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    //springboot在自定义拦截器中使用@Value获取值失败,原因是拦截器对象并没有交给容器，可以在实现WebMvcConfig中将AccessInterceptor拦截器对象注入到容器中
    //在项目中定义了多个bean,加载时会造成bean冲突，需要给bean加上唯一的标识，或者在yml中添加配置: spring.main.allow-bean-definition-overriding=true
    //将自定义拦截器对象交给spring管理
    @Bean(value = "accessInterceptorBean")
    public AccessInterceptor accessInterceptor() {
        return new AccessInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截的接口路径
        String[] addPathPatterns = {
                "/rsb/**"
        };
//        //排除的路径
        String[] excludePathPatterns = {"/user/error"};
//        String[] excludePathPatterns = {
//                "/user/login","/user/noLg","/user/error"
//        };
//      registry.addInterceptor(new AccessInterceptor()).addPathPatterns(addPathPatterns).excludePathPatterns(excludePathPatterns);
        //创建用户拦截器对象并指定其拦截的路径和排除的路径
        //将AccessInterceptor拦截器添加到拦截器链中，不要重新new拦截器对象
//        registry.addInterceptor(accessInterceptor()).addPathPatterns(addPathPatterns);
        registry.addInterceptor(accessInterceptor()).addPathPatterns(addPathPatterns).excludePathPatterns(excludePathPatterns);
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
