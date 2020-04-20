package com.firewolf.log.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "lx.log")
public class LogProperties {
    /**
     * 可以通过设置为db来激活默认的数据库存储方式
     */
    private String handler;
    /**
     * 取值true,false
     */
    private String auto;
}

