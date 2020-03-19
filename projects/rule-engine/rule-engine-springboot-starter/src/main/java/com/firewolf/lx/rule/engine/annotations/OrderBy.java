package com.firewolf.lx.rule.engine.annotations;

import com.firewolf.lx.rule.engine.enums.OrderType;

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
