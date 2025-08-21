package com.oa5.config;

import com.oa5.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @name: 余仕贤
 * @Date: 2021/12/24 16:44
 * @Author: IAO
 * @Description: 登录验证
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Bean
    public LoginInterceptor loginInterceptor(){
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加登录验证拦截器
        registry.addInterceptor(loginInterceptor())
                .excludePathPatterns("/")
                .excludePathPatterns("/login")
                .excludePathPatterns("/register")
                .excludePathPatterns("/static/**");
    }

    //需要告知系统，这是要被当成静态文件的！
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //第一个方法设置访问路径前缀，第二个方法设置资源路径
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}
