package com.firewolf.thread.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Author: liuxing
 * Date: 2020/2/28 9:57
 * 线程池配置
 */
@Configuration
@ConfigurationProperties(prefix = "lx.log.thread.pool")
@Data
public class ExecutorProperties {
    /**
     * 核心线程数
     */
    private Integer core = 10;
    /**
     * 最大线程数
     */
    private Integer max = 50;

    /**
     * 队列容量
     */
    private Integer capacity = 99999;

    /**
     * 线程池名字前缀
     */
    private String name = "lx-service";

}
