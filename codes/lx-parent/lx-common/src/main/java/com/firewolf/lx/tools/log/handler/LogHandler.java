package com.firewolf.lx.tools.log.handler;

import com.firewolf.lx.tools.log.LogPO;

/**
 * Author: liuxing
 * Date: 2020/2/28 9:34
 * 默认的日志建表语句：
     CREATE TABLE `lx_log` (
     `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
     `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `modify_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
     `version` int DEFAULT '0' COMMENT '版本号',
     `deleted` int DEFAULT '0' COMMENT '删除标志',
     `start` varchar(200) DEFAULT NULL COMMENT '开始描述',
     `end` varchar(200) DEFAULT NULL COMMENT '结束描述',
     `operate` varchar(100) DEFAULT NULL COMMENT '操作描述',
     `operator` varchar(50) DEFAULT NULL COMMENT '操作者',
     `method` varchar(100) DEFAULT NULL COMMENT '执行的方法',
     `parms` varchar(200) DEFAULT NULL COMMENT '执行方法的参数',
     `error` text COMMENT '错误描述',
     `result` text COMMENT '结果',
     `start_time` datetime DEFAULT NULL COMMENT '开始时间',
     `end_time` datetime DEFAULT NULL COMMENT '结束时间',
     PRIMARY KEY (`id`)
     ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
 */
public interface LogHandler<T> {

    /**
     * 把日志对象转换成自己需要的类型
     *
     * @param logPO
     * @return
     */
    default T transLog2SelfObj(LogPO logPO) {
        return null;
    }

    /**
     * 对日志的处理
     *
     * @param log 日志对象，默认为LogPO，但是用户可以根据自己的需求进行属性转换
     */
    void handle(T log);
}
