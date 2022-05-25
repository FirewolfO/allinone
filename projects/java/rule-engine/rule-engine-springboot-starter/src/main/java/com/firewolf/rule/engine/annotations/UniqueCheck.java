package com.firewolf.rule.engine.annotations;

import com.firewolf.rule.engine.enums.UniqueType;

import java.lang.annotation.*;

/**
 * 唯一检查，
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UniqueCheck {
    UniqueType value() default UniqueType.Only;
}
