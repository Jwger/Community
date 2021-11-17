package com.nowcoder.community.config;

import com.nowcoder.community.controller.interceptor.AlphaInterceptor;
import com.nowcoder.community.controller.interceptor.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig  implements WebMvcConfigurer {

    @Autowired
    private AlphaInterceptor alphaInterceptor;

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截器
        registry.addInterceptor(alphaInterceptor)
                //哪些资源可以不用拦截
                //这些静态资源都不需要拦截
                .excludePathPatterns("/**/*.css","/**/*.js",
                        "/**/*.jpg","/**/*.png","/**/*.jpeg")
                //指定拦截哪些资源
                .addPathPatterns("/register","/login");

        registry.addInterceptor(loginTicketInterceptor)
                //哪些资源可以不用拦截
                //这些静态资源都不需要拦截
                .excludePathPatterns("/**/*.css","/**/*.js",
                        "/**/*.jpg","/**/*.png","/**/*.jpeg");
    }
}
