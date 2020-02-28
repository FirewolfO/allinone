package com.firewolf.lx.config;

import com.alibaba.ttl.threadpool.TtlExecutors;
import com.firewolf.lx.tools.log.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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

    /**
     * 默认的获取日志操作者的Bean
     * @return
     */
    @ConditionalOnMissingBean(value = {LogOperator.class})
    @Bean
    public LogOperator defaultLogOperator() {
        return new DefaultLogOperator();
    }

    /**
     * 默认日志处理器，当用户没有指定，且开了默认日志处理器的时候，才会装装载
     * @return
     */
    @ConditionalOnProperty(name = "log.handler.default.enable", havingValue = "true")
    @ConditionalOnMissingBean(value = {LogHandler.class})
    @Bean
    public LogHandler defaultLogHandler() {
        return new DefaultLogHandler();
    }


    /**
     * 有日志处理器且有日志操作者的时候，才会开启日志功能
     * @return
     */
    @Bean
    @ConditionalOnBean(value = {LogHandler.class,LogOperator.class})
    public LogResolver enableLog(){
        return new LogResolver();
    }
}
