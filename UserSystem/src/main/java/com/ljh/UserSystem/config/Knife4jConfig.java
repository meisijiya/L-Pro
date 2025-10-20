package com.ljh.UserSystem.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {
    // 配置文档信息
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI().info(new Info()
                .title("文档标题")
                .version("1.0")
                .description("文档描述")
        );
    }

    // 配置功能分组，路由可以有多个
    @Bean
    public GroupedOpenApi typeAPI(){
        return GroupedOpenApi.builder().group("类型分组")
                .pathsToMatch("/type/**", "/test/**")
                .build();
    }
}
