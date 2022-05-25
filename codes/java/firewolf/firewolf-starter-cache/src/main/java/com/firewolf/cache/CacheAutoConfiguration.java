package com.firewolf.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述：
 * Author：liuxing
 * Date：2020-08-05
 */
@Configuration
public class CacheAutoConfiguration {
    @Bean
    public CacheAspect cacheAspect() {
        return new CacheAspect();
    }
}
