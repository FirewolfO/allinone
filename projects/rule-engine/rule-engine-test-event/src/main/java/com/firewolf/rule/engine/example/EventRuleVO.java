package com.firewolf.rule.engine.example;

import lombok.Data;

import java.util.List;

@Data
public class EventRuleVO {

    /**
     * 规则名称
     */
    private String name;

    /**
     * 事件类型
     */
    private List<Integer> eventTypes;

    /**
     * 设备ID集合
     */
    private List<String> deviceIds;

    /**
     * 事件等级
     */
    private Integer eventLevel;

    /**
     * 事件计划
     */
    private String timePlan;

    /**
     * 联动
     */
    private String linkage;

    /**
     * 是否开启
     */
    private Integer isEnable;

    /**
     * id
     */
    private Integer id;
}

