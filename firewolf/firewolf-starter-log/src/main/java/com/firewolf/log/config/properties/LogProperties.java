package com.firewolf.log.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "firewolf.log")
public class LogProperties {
    /**
     * 可以通过设置为db来激活默认的数据库存储方式
     */
    private String out;
    /**
     * 取值true,false
     */
    private String enable;

    /**
     * 是否在没有指定记录参数的时候记录所有参数
     */
    private boolean allArgs = true;

    /**
     * 是否在某没有指定记录结果的时候记录所有结果
     */
    private boolean resAll = true;

    /**
     * 操作成功描述
     */
    private String successDesc = "操作成功";

    /**
     * 操作失败描述
     */
    private String errorDesc = "操作失败";
}

