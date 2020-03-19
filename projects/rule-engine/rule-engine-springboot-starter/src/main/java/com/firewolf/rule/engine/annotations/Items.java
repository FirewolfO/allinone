package com.firewolf.rule.engine.annotations;

import java.lang.annotation.*;

/**
 * 规则项注解
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Items {
}
