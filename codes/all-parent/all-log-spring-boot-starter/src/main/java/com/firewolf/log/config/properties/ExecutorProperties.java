package com.firewolf.log.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Author: liuxing
 * Date: 2020/2/28 9:57
 * 线程池配置
 */
@ConfigurationProperties(prefix = "lx.log.thread.pool")
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

    public Integer getCore() {
        return core;
    }

    public void setCore(Integer core) {
        this.core = core;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
