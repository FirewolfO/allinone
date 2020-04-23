package com.firewolf.log.annotations;

import java.lang.annotation.*;

/**
 * Author: liuxing
 * Date: 2020/2/28 8:45
 * 业务日志注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpLog {

    /**
     * 操作，支持SpEL表达式来使用参数，格式需要引入括号，如：
     * delete user name is #{user.name}
     *
     * @return
     */
    String value() default "";

    /**
     * 操作
     *
     * @return
     */
    String operate() default "";//操作


    /**
     * 操作参数，支持使用SpEL 表达式，如：
     * name=#{user.name},
     * carColor=#{user.car?.color}，
     * userAge={#user.age+10}
     * userNameHello=#{user.name+'hello'}
     * 等等
     *
     * @return
     */
    String[] params() default {};

    /**
     * 操作者
     *
     * @return
     */
    String operator() default "";


    /**
     * 操作结果，支持通过SpEL表达式从操作结果里面获取数据，通过res访问结果对象
     * 如：
     * #{res.name}
     *
     * @return
     */
    String result() default "";


}
