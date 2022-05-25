package com.firewolf.log.config.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Author: liuxing
 * Date: 2020/2/28 16:27
 */
@Data
@ConditionalOnProperty(prefix = "firewolf.log", name = "out", value = "mysql")
@ConfigurationProperties(prefix = "spring.datasource")
public class DBProperties {

    private String url;

    private String username;
    
    private String password;

    private String logTable;

    private String driverClassName = "com.mysql.cj.jdbc.Driver";

}
