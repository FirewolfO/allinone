package com.firewolf.lx.rule.engine.annotations;

import java.lang.annotation.*;

/**
 * 规则规则外键
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ForeignKey {

}
