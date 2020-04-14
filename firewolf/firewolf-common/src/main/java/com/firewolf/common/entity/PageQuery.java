package com.firewolf.common.entity;

import lombok.Data;

/**
 * Description: 分页查询对象
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/4/14 11:57 下午
 */
@Data
public class PageQuery {

    /**
     * 每页显示条数
     */
    private Integer pageSize = 10;

    /**
     * 当前页，如果传入的值小于等于0，则不分页
     */
    private Integer pageNum = 1;

}
