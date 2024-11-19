package com.kosa.mini.api.security;

import com.kosa.mini.mvc.interceptor.AdminInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AdminInterceptor adminInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/uploads/**")
                .allowedOrigins("http://localhost:5173"); // 프론트엔드 도메인
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**"); // /admin/** 경로에 인터셉터 적용
        //.excludePathPatterns("/admin/login", "/admin/register"); // 필요에 따라 제외할 경로 설정
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/stores/**")
                .addResourceLocations("file:C:/uploads/stores/");
        registry.addResourceHandler("/uploads/menus/**")
                .addResourceLocations("file:C:/uploads/menus/");
    }

}
