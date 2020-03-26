package com.firewolf.log.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Author: liuxing
 * Date: 2020/2/28 16:27
 */
@Data
@ConfigurationProperties(prefix = "log.db")
public class DBProperties {

    private String host = "locahost";

    private int post = 3306;

    private String datasoure = "lx-log";

    private String username = "root";

    private String password;

    private String table = "lx_log";

    private String driver = "com.mysql.jdbc.Driver";

    public String getUrl() {
        String s = "jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC&useSSL=false";
        return String.format(s, host, post, datasoure);
    }

}
