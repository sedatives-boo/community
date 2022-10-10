package com.nowcoder.community.config;

import com.nowcoder.community.controller.interceptor.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * 1、配置拦截器，需要配置类实现接口WebMvcConfigurer
 * 2、并注入相应接口
 * 3、实现addInterceptors，将拦截器注入
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {



    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {


        registry.addInterceptor(loginTicketInterceptor).
                excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");  //不拦截静态资源

    }


}
