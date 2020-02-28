package com.firewolf.lx.entity;

import lombok.Data;

/**
 * Author: liuxing
 * Date: 2020/2/25 16:07
 * 基础查询对象
 */
@Data
public class BaseQuery {

    /**
     * 每页条数
     */
    private Integer pageSize;

    /**
     * 当前页
     */
    private Integer currentPage;

    /**
     * 起始页
     * @return
     */
    public Integer getStart() {
        return (currentPage - 1) * pageSize;
    }
}
