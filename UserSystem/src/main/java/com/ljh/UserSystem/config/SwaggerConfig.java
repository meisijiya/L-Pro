package com.ljh.UserSystem.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("LJH-UserSystem接口文档")
                        .contact(new Contact()
                                .name("meisijiya")
                                .email("2292360909@qq.com")
                                .url("https://github.com/meisijiya"))
                        .description("LJH-UserSystem接口文档")
                        .version("v1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
//                .externalDocs(new ExternalDocumentation()
//                        .description("外部文档")
//                        .url(""));
    }
}


