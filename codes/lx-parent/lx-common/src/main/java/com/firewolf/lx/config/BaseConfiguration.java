package com.firewolf.lx.config;

import com.alibaba.ttl.threadpool.TtlExecutors;
import com.firewolf.lx.config.ExecutorProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Author: liuxing
 * Date: 2020/2/28 9:44
 * 一些基础工具的配置
 */
@Configuration
public class BaseConfiguration {

    @Autowired
    private ExecutorProperties executorProperties;

    /**
     * 线程池配置
     * @return
     */
    @Bean("lx_executor")
    public Executor getExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(executorProperties.getCore());
        executor.setMaxPoolSize(executorProperties.getMax());
        executor.setQueueCapacity(executorProperties.getCapacity());
        executor.setThreadNamePrefix(executorProperties.getName());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        Executor ttlExecutor = TtlExecutors.getTtlExecutor(executor);
        return ttlExecutor;

    }
}
