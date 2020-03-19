package com.firewolf.rule.engine.core;

public class DefaultRuleMatcher implements IRuleMatcher {
    @Override
    public boolean match(Object rule, Object data) {
        return true;
    }
}

