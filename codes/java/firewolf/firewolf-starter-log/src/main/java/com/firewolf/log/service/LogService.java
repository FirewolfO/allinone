package com.firewolf.log.service;

import java.util.List;

/**
 * Author: liuxing
 * Date: 2020/3/1 21:46
 * 日志Service，用于日志的增删改查
 */
public interface LogService {

    List<Object> list();

    void save(Object log);
}
