package com.firewolf.config;

import com.firewolf.tools.log.LogResolver;
import com.firewolf.tools.log.service.DBLogService;
import com.firewolf.tools.log.handler.DefaultDBLogHandler;
import com.firewolf.tools.log.handler.DefaultLogHandler;
import com.firewolf.tools.log.handler.LogHandler;
import com.firewolf.tools.log.operator.DefaultLogOperator;
import com.firewolf.tools.log.operator.LogOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Author: liuxing
 * Date: 2020/3/1 21:28
 */
@Configuration
public class LogConfiguration {

    @Autowired
    private LogDBProperties properties;

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
    @ConditionalOnProperty(name = "log.handler", havingValue = "default")
    @ConditionalOnMissingBean(value = {LogHandler.class})
    @Bean
    public LogHandler defaultLogHandler() {
        return new DefaultLogHandler();
    }

    /**
     * 默认的把日志存放到数据的处理器
     * @return
     */
    @ConditionalOnProperty(name = "log.handler", havingValue = "db")
    @ConditionalOnMissingBean(value = {LogHandler.class})
    @Bean
    public LogHandler defaultDBLogHandler() {
        return new DefaultDBLogHandler();
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


    @Bean
    @ConditionalOnProperty(name = "log.handler", havingValue = "db")
    public JdbcTemplate jdbcTemplate(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(properties.getDriver());
        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        jdbcTemplate.setDataSource(dataSource);
        return  jdbcTemplate;
    }

    @Bean
    @ConditionalOnProperty(name = "log.handler", havingValue = "db")
    public DBLogService dblogService(){
        return new DBLogService();
    }
}
