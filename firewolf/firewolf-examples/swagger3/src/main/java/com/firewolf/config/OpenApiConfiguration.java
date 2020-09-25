package com.firewolf.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述：
 * Author：liuxing
 * Date：2020-09-22
 */
@Configuration
public class OpenApiConfiguration {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .setGroup("springshop-public")
                .packagesToScan("com.firewolf.web")
                .build();
    }

}
