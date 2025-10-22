package com.ljh.UserSystem.config;

import com.ljh.UserSystem.config.Interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
//    @Resource
//    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录拦截器
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(
                        "/user/login",
                        "/user/register",
                        "/swagger-ui/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/doc.html/**"
                ).order(1);
    }
}