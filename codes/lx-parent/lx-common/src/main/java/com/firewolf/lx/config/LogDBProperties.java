package com.firewolf.lx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Author: liuxing
 * Date: 2020/2/28 16:27
 */
@ConfigurationProperties(prefix = "log.db")
@Configuration
@Data
public class LogDBProperties {

    private String host = "locahost";

    private int post = 3306;

    private String datasoure="lx-log";

    private String username = "root";

    private String password;

    private String table = "lx_log";

    public String getUrl() {
        String s = "jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC";
        return String.format(s, host, post, datasoure);
    }

}
