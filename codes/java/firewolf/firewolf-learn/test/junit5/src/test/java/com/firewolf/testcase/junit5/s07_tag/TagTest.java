package com.firewolf.testcase.junit5.s07_tag;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述：Tag与过滤，如果类上面标注了，那么方法上面的会失效
 * Author：liuxing
 * Date：2020/6/20
 */
//@Tags({
//        @Tag("dev"),
//        @Tag("test")
//})
public class TagTest {


    @Tag("test")
    @Test
    void test1() {
        System.out.println("test1");
    }

    @Tags({
            @Tag("dev"),
            @Tag("test")
    })
    @Test
    void test2() {
        System.out.println("test2");
    }


    @Dev
    void test3() {
        System.out.println("test3");
    }
}

// 自定义Tag标签，可以通过集成@Tag和@Test，让我们的测试方法或者类上面去掉不需要的@Test
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Tag("dev")
@Test
@interface Dev {

}