package com.firewolf.rule.engine.annotations;

import java.lang.annotation.*;

/**
 * 规则ID注解
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Id {
}
