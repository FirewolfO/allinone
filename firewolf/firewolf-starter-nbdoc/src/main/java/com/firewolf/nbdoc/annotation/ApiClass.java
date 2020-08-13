package com.firewolf.nbdoc.annotation;

import java.lang.annotation.*;

/**
 * Description: Api类描述
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/8/10 11:17 上午
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiClass {

    /**
     * 当前接口类描述
     *
     * @return
     */
    String description() default "";

    /**
     * 所属分类，多级分类之间使用,隔开
     *
     * @return
     */
    String[] groups() default {""};
}
