package com.firewolf.rule.example.cars;

import com.firewolf.rule.engine.core.RuleEngine;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CarRuleEngine extends RuleEngine<CarRule, CarRule, LocalDateTime> {
}
