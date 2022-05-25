package com.firewolf.swagger.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/4/15
 */
@ConfigurationProperties(prefix = "firewolf.swagger")
@Data
public class SwaggerConfig {

    /**
     * 是否启用
     */
    private boolean enable = true;

    /**
     * 标题
     */
    private String title = "遮不住的殇项目文档";

    /**
     * 文档描述
     */
    private String description = "这是遮不住的殇提供的文档!!!";

    /**
     * 项目版本
     */
    private String version = "v1";


}
