package com.firewolf.lx.rule.engine.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@ConfigurationProperties(prefix = "lx.rule")
@Configuration
@Data
public class RuleProperties {

    /**
     * 匹配规则的组合方式，可选值有and和or
     */
    private String matchType = "and";

    /**
     * 规则的唯一性属性
     */
    private List<String> uniqueColumns = null;

    /**
     * 冲突之后的解策略，可选值，cover覆盖，discard--丢弃，merge--合并
     */
    private String conflictStrategy = "cover";

    /**
     * 用来查找规则的列
     */
    private List<String> searchColumn;
}
