package com.ransibi.config;

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
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截的接口路径
        String[] addPathPatterns = {
                "/rsb/**"
        };
//        //排除的路径
//        String[] excludePathPatterns = {
//                "/user/login","/user/noLg","/user/error"
//        };
//      registry.addInterceptor(new AccessInterceptor()).addPathPatterns(addPathPatterns).excludePathPatterns(excludePathPatterns);
        //创建用户拦截器对象并指定其拦截的路径和排除的路径
        registry.addInterceptor(new AccessInterceptor()).addPathPatterns(addPathPatterns);
    }
}
