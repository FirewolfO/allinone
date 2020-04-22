package com.firewolf.log.config;

import com.alibaba.ttl.threadpool.TtlExecutors;
import com.firewolf.log.config.properties.ExecutorProperties;
import com.firewolf.log.config.properties.LogProperties;
import com.firewolf.log.core.LogProcessor;
import com.firewolf.log.core.ParamParser;
import com.firewolf.log.handler.DBLogHandler;
import com.firewolf.log.handler.DefaultLogHandler;
import com.firewolf.log.handler.LogHandler;
import com.firewolf.log.operator.DefaultLogOperator;
import com.firewolf.log.operator.LogOperator;
import com.firewolf.log.service.DBLogService;
import com.firewolf.log.config.properties.DBProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 日志自动装配类
 */
@Configuration
@ConditionalOnProperty(
        prefix = "lx.log",
        name = "auto",
        havingValue = "true",
        matchIfMissing = true
)
@EnableConfigurationProperties({DBProperties.class, ExecutorProperties.class, LogProperties.class})
public class LogAutoConfiguration {
    /**
     * 默认的获取日志操作者的Bean
     *
     * @return
     */
    @ConditionalOnMissingBean(value = {LogOperator.class})
    @Bean
    public LogOperator defaultLogOperator() {
        return new DefaultLogOperator();
    }

    /**
     * 默认日志处理器，当用户没有指定，且开了默认日志处理器的时候，才会装装载
     *
     * @return
     */
    @ConditionalOnMissingBean(value = {LogHandler.class})
    @Bean
    public LogHandler defaultLogHandler() {
        return new DefaultLogHandler();
    }

    /**
     * 默认的把日志存放到数据的处理器
     *
     * @return
     */
    @ConditionalOnProperty(name = "lx.log.handler", havingValue = "db")
    @Bean
    public LogHandler defaultDBLogHandler() {
        return new DBLogHandler();
    }


    /**
     * 有日志处理器且有日志操作者的时候，才会开启日志功能
     *
     * @return
     */
    @Bean
    @ConditionalOnBean(value = {LogHandler.class, LogOperator.class})
    public LogProcessor logProcessor() {
        return new LogProcessor();
    }


    @Bean
    @ConditionalOnProperty(name = "lx.log.handler", havingValue = "db")
    public JdbcTemplate jdbcTemplate(DBProperties properties) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(properties.getDriver());
        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }

    @Bean
    @ConditionalOnProperty(name = "lx.log.handler", havingValue = "db")
    public DBLogService dblogService() {
        return new DBLogService();
    }

    @Bean
    public ParamParser paramParser(){
        return new ParamParser();
    }

    @Bean
    public ParameterNameDiscoverer parameterNameDiscoverer(){
        return new DefaultParameterNameDiscoverer();
    }

    /**
     * 线程池配置
     *
     * @return
     */
    @Bean("lx_executor")
    public Executor getExecutor(ExecutorProperties executorProperties) {
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
