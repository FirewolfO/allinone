package com.firewolf.tools.log;

import com.firewolf.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Author: liuxing
 * Date: 2020/2/28 9:27
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Log extends BaseEntity {
    /**
     * 开始描述
     */
    private String start;
    /**
     * 结束描述
     */
    private String end;
    /**
     * 具体操作
     */
    private String operate;

    /**
     * 操作者
     */
    private String operator;

    /**
     * 执行的方法
     */
    private String method;
    /**
     * 方法传入的参数
     */
    private String parms;

    /**
     * 执行产生的错误
     */
    private String error;

    /**
     * 执行的结果
     */
    private String result;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}
