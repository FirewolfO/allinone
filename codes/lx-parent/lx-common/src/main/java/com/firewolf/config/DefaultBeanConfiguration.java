package com.firewolf.config;

import com.alibaba.ttl.threadpool.TtlExecutors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Author: liuxing
 * Date: 2020/2/28 9:44
 * 默认Bean配置类
 */
@Configuration
public class DefaultBeanConfiguration {

    @Autowired
    private ExecutorProperties executorProperties;


    /**
     * 线程池配置
     *
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
