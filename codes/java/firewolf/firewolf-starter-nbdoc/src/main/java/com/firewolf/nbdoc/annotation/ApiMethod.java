package com.firewolf.nbdoc.annotation;

import java.lang.annotation.*;

/**
 * Description: Api方法描述
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/8/10 11:20 上午
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiMethod {
    String desciption() default "";
}
