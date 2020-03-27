package com.firewolf.rule.engine.example;

import com.firewolf.rule.engine.core.matcher.IRuleMatcher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TimeRuleMatcher implements IRuleMatcher<EventRule, LocalDateTime> {
    @Override
    public boolean match(EventRule rule, LocalDateTime data) {
        return true;
    }
}
