package com.firewolf.lx.rule.engine.config;

import com.firewolf.lx.rule.engine.core.*;
import com.firewolf.lx.rule.engine.core.conflict.resolver.AbstractConflictResolver;
import com.firewolf.lx.rule.engine.core.conflict.resolver.DefaultConflictResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(
        prefix = "lx.rule",
        name = {"auto"},
        havingValue = "true",
        matchIfMissing = true
)
@EnableConfigurationProperties({RuleProperties.class, DataSourceProperties.class})
public class RuleAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean(value = RuleEngine.class)
    public RuleEngine ruleEngine() {
        return new RuleEngine();
    }

    @Bean
    @ConditionalOnMissingBean(value = IRuleMatcher.class)
    public IRuleMatcher defaultRuleMather() {
        return new DefaultRuleMatcher();
    }

    @Bean
    @ConditionalOnMissingBean(value = IRuleService.class)
    public IRuleService defaultRuleService() {
        return new DefaultRuleService();
    }


    @Bean
    @ConditionalOnMissingBean(value = AbstractConflictResolver.class)
    public AbstractConflictResolver defaultConflictResolver() {
        return new DefaultConflictResolver();
    }


    @Bean
    public NamedParameterJdbcTemplate dataSource(DataSourceProperties dataSourceProperties) {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(dataSourceProperties.getDriverClassName());
        dataSourceBuilder.url(dataSourceProperties.getUrl());
        dataSourceBuilder.username(dataSourceProperties.getUsername());
        dataSourceBuilder.password(dataSourceProperties.getPassword());
        DataSource dataSource = dataSourceBuilder.build();
        return new NamedParameterJdbcTemplate(dataSource);
    }

}
