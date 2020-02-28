package com.firewolf.lx.service;

import com.firewolf.lx.dao.LogDao;
import com.firewolf.lx.tools.log.LogPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: liuxing
 * Date: 2020/2/28 15:43
 */
@Service
public class LogService {

    @Autowired
    private LogDao logDao;


    public List<LogPO> findLogs() {
        return logDao.findByConditon(null);
    }
}
