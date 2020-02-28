package com.firewolf.lx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Author: liuxing
 * Date: 2020/2/28 16:27
 */
@ConfigurationProperties(prefix = "log.db")
@Component
@Data
public class LogDBProperties {

    private String host;

    private int post = 3306;

    private String datasoure;

    private String username;

    private String password;

    private String table;

    private String colums;


    public String getUrl() {
        String s = "jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC";
        return String.format(s, host, post, datasoure);
    }

}
