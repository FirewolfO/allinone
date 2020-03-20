package com.firewolf.rule.engine.example;

import com.firewolf.rule.engine.core.RuleEngine;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventRuleEngine extends RuleEngine<EventRule, EventRuleItem, LocalDateTime> {

}