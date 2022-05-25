package com.firewolf.rule.engine.annotations;

import com.firewolf.rule.engine.enums.OrderType;

import java.lang.annotation.*;

/**
 * 排序字段
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OrderBy {
    OrderType value() default OrderType.DESC;
}
