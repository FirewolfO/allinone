package com.firewolf.jwt.config;

import com.firewolf.jwt.config.properties.JWTConfig;
import com.firewolf.jwt.utils.SpringContextUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/4/23 7:04 上午
 */
@Configuration
@ConditionalOnProperty(prefix = "firewolf.jwt",name = "auto",havingValue = "true",matchIfMissing = true)
@EnableConfigurationProperties(JWTConfig.class)
public class AutoJWTConfiguration {

    @Bean
    public SpringContextUtil springContextUtil(){
        return new SpringContextUtil();
    }
}
