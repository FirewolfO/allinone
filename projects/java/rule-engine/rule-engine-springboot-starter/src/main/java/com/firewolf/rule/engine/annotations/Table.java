package com.firewolf.rule.engine.annotations;

import java.lang.annotation.*;

/**
 * 规则表
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {
    String value() default "";  // 表名
}
