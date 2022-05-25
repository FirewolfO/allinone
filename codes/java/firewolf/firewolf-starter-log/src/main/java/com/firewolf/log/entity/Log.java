package com.firewolf.log.entity;

import com.firewolf.common.entity.BaseEntity;
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
    private String params;

    /**
     * 执行产生的错误
     */
    private String error;

    /**
     * 执行的结果
     */
    private String result;

    /**
     * 结果状态，success表示成功，error表示失败
     */
    private String resultStatus;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 耗时毫秒数
     */
    private Long timeCost;
}
