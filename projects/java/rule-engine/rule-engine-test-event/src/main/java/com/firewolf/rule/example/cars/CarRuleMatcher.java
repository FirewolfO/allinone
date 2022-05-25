package com.firewolf.rule.example.cars;

import com.firewolf.rule.engine.core.matcher.IRuleMatcher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CarRuleMatcher implements IRuleMatcher<CarRule, LocalDateTime> {
    @Override
    public boolean match(CarRule rule, LocalDateTime data) {
        System.out.println("car rule matcher....");
        return true;
    }
}
