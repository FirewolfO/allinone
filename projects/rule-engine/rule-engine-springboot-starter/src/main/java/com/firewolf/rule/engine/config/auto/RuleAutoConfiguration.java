package com.firewolf.rule.engine.config.auto;

import com.firewolf.rule.engine.config.properties.DataSourceProperties;
import com.firewolf.rule.engine.config.properties.RuleProperties;
import com.firewolf.rule.engine.core.*;
import com.firewolf.rule.engine.core.conflict.resolver.AbstractConflictResolver;
import com.firewolf.rule.engine.core.conflict.resolver.CoverConflictResolver;
import com.firewolf.rule.engine.core.conflict.resolver.DefaultConflictResolver;
import com.firewolf.rule.engine.core.matcher.DefaultRuleMatcher;
import com.firewolf.rule.engine.core.matcher.IRuleMatcher;
import com.firewolf.rule.engine.service.DefaultRuleService;
import com.firewolf.rule.engine.service.IRuleService;
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


    @ConditionalOnProperty(prefix = "lx.rule", name = "conflict-strategy", havingValue = "cover")
    @Bean
    public AbstractConflictResolver coverConflictResolver() {
        return new CoverConflictResolver();
    }

    @Bean
    @ConditionalOnMissingBean(AbstractConflictResolver.class)
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
