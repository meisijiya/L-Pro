package com.ljh.UserSystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")// 配置CORS策略：允许所有路径的跨域请求
                .allowCredentials(true)// 允许携带认证信息（如Cookie）
                .allowedOriginPatterns(allowedOrigins)// 区分源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许常用的HTTP方法
                // 允许所有请求头和响应头
                .allowedHeaders("*")
                .exposedHeaders("*")
        ;
    }
}
