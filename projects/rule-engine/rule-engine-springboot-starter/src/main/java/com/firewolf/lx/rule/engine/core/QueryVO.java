package com.firewolf.lx.rule.engine.core;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 查询对象
 */
@Data
public class QueryVO {

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
