package com.firewolf.rule.example.event;

import com.firewolf.rule.engine.annotations.*;
import com.firewolf.rule.engine.enums.UniqueType;
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
    @UniqueCheck(UniqueType.Union)
    private Integer eventType;

    /**
     * 设备ID
     */
    @UniqueCheck(UniqueType.Union)
    private String deviceId;


}
