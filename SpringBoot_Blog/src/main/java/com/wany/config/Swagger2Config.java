package com.wany.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class Swagger2Config {
    @Bean
    public Docket docket(){
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .enable(true)
                .groupName("wany")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wany.controller"))
                .build();
        return docket;
    }
}
