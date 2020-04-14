package com.firewolf.rule.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 查询对象，
 * 如果是单表结构，就直接放到mainParams中就可以了
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleQuery {

    /**
     * 每页显示数量
     */
    private Integer pageSize = 10;

    /**
     * 当前页
     */
    private Integer currentPage = 1;

    /**
     * 主表查询参数
     */
    private Map<String, Object> mainParams = new HashMap<>();

    /**
     * 子表查询参数
     */
    private Map<String, Object> subParams = new HashMap<>();


    public Integer getStart() {
        return (currentPage - 1) * pageSize;
    }
}
