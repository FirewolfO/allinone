package com.firewolf.swagger.config;

import com.spring4all.swagger.EnableSwagger2Doc;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Author: liuxing
 * Date: 2020/3/3 17:16
 */

@Configuration
@EnableSwagger2Doc
@EnableConfigurationProperties({LXSwaggerProperties.class})
@ConditionalOnProperty(
        prefix = "lx.swagger",
        name = "auto",
        havingValue = "true",
        matchIfMissing = true
)
public class SwaggerAutoConfiguration {

    @Bean
    public Docket createRestApi(LXSwaggerProperties lxSwaggerProperties) {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo(lxSwaggerProperties))
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();
    }

    //基本信息的配置，信息会在api文档上显示
    private ApiInfo apiInfo(LXSwaggerProperties lxSwaggerProperties) {
        return new ApiInfoBuilder()
                .title(lxSwaggerProperties.getTitle())
                .version(lxSwaggerProperties.getVersion())
                .build();
    }
}
