package com.firewolf.thread;

import com.alibaba.ttl.threadpool.TtlExecutors;
import com.firewolf.thread.config.ExecutorProperties;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 描述：线程池工厂
 * Author：liuxing
 * Date：2020/4/20
 */
public class ThreadPoolFactory {


    public static Executor getTTLExecutor(ExecutorProperties executorProperties) {
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
