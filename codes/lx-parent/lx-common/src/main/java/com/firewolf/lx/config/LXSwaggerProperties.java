package com.firewolf.lx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Author: liuxing
 * Date: 2020/3/3 18:07
 */
@Configuration
@ConfigurationProperties(prefix = "lx.swagger")
@Data
public class LXSwaggerProperties {

    private String title = "lx接口文档";

    private String version = "1.0";
}
