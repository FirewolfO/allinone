package com.lx.event;

import lombok.Data;

import java.util.Date;
import java.util.List;

import com.firewolf.rule.engine.annotations.*;

@Data
@Table("event_rule")
public class EventRule {

    /**
     * 规则ID
     */
    @Id
    private Integer id;

    /**
     * 规则名
     */
    @Like
    private String name;

    /**
     * 时间计划
     */
    private String timePlan;

    /**
     * 规则项
     */
    @Items
    private List<EventRuleItem> ruleItems;

    /**
     * 联动
     */
    @Column("event_linkage_types")
    private String linkage;

    /**
     * 是否开启
     */
    private Integer isEnable;

    /**
     * 创建时间
     */
    @OrderBy
    private Date gmtCreate;
}
