package com.firewolf.lx.tools.log.service;

import java.util.List;

/**
 * Author: liuxing
 * Date: 2020/3/1 21:46
 */
public interface LogService {

    List<Object> list();

    void save(Object log);
}
