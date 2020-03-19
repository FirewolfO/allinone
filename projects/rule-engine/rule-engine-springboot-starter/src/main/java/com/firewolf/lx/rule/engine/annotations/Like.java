package com.firewolf.lx.rule.engine.annotations;

import com.firewolf.lx.rule.engine.enums.LikeType;

import java.lang.annotation.*;

/**
 * like 字段
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Like {
    LikeType value() default LikeType.all;
}
