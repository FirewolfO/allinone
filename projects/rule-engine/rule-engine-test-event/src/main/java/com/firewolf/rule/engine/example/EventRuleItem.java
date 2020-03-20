package com.firewolf.rule.engine.example;

import com.firewolf.rule.engine.annotations.*;
import lombok.Data;

@Table("event_rule_item")
@Data
public class EventRuleItem {

    @Id
    private Integer id;

    @ForeignKey
    private Integer ruleId;

    /**
     * 事件类型
     */
    private Integer eventType;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 事件等级
     */
    @Column("event_level_id")
    private Integer eventLevel;

}
