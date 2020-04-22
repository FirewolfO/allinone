package com.firewolf.log.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Author: liuxing
 * Date: 2020/2/28 16:27
 */
@Data
@ConfigurationProperties(prefix = "firewolf.log.db")
public class DBProperties {

    private String host = "locahost";

    private int post = 3306;

    private String datasoure = "firewolf-log";

    private String username = "root";

    private String password = "root";

    private String table = "firewolf_log";

    private String driver = "com.mysql.cj.jdbc.Driver";

    public String getUrl() {
        String s = "jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC&useSSL=false";
        return String.format(s, host, post, datasoure);
    }

}
