package com.firewolf.swagger.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Author: liuxing
 * Date: 2020/3/3 18:07
 */
@ConfigurationProperties(prefix = "lx.swagger")
public class LXSwaggerProperties {

    private String title = "lx接口文档";

    private String version = "1.0";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
