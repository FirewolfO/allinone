package com.firewolf.swagger.config;

import com.firewolf.swagger.config.properties.SwaggerConfig;
import io.swagger.annotations.Api;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/4/15
 */
@Configuration
@EnableSwagger2
@EnableConfigurationProperties(SwaggerConfig.class)
public class AutoSwaggerConfiguration {


    @Bean
    public Docket createRestApi(SwaggerConfig swaggerConfig) {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerConfig.isEnable())
                .apiInfo(apiInfo(swaggerConfig))
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();
    }

    //基本信息的配置，信息会在api文档上显示
    private ApiInfo apiInfo(SwaggerConfig swaggerConfig) {
        return new ApiInfoBuilder()
                .title(swaggerConfig.getTitle())
                .description(swaggerConfig.getDescription())
                .version(swaggerConfig.getVersion())
                .build();
    }

}
