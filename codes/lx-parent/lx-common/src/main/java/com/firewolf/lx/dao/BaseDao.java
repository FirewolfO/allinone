package com.firewolf.lx.dao;

import com.firewolf.lx.entity.BaseQuery;

import java.util.List;

/**
 * Author: liuxing
 * Date: 2020/2/25 16:01
 * 持久化操作
 */
public interface BaseDao<T> {

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    T findById(Integer id);


    /**
     * 按条件查询列表
     *
     * @param query
     * @return
     */
    List<T> findByConditon(BaseQuery query);

}
