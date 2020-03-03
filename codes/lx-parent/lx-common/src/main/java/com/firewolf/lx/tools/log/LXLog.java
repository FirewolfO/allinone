package com.firewolf.lx.tools.log;

import java.lang.annotation.*;

/**
 * Author: liuxing
 * Date: 2020/2/28 8:45
 * 业务日志注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LXLog {
    String start() default ""; // 方法开始执行前日志

    String end() default ""; // 方法结束执行日志

    String operate() default "";//操作

    String error() default "";//错误日志
}
