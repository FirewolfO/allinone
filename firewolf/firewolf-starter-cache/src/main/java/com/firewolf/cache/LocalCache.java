package com.firewolf.cache;

import java.lang.annotation.*;

/**
 * 描述：本地缓存
 * Author：liuxing
 * Date：2020-08-05
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LocalCache {

    String key();
    
    String desc();
}
