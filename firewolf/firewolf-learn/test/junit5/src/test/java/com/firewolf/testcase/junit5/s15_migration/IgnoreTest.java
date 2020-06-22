package com.firewolf.testcase.junit5.s15_migration;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.migrationsupport.EnableJUnit4MigrationSupport;

/**
 * 描述：使用junit4中的Rule，需要引入junit-jupiter-migrationsupport依赖，下面类上面的注解可以不加
 * Author：liuxing
 * Date：2020/6/22
 */
@EnableJUnit4MigrationSupport
//@ExtendWith({IgnoreCondition.class})
public class IgnoreTest {

    @Ignore
    @Test
    public void test1(){
        System.out.println("test1");
    }

    @Test
    public void test2(){
        System.out.println("test2");
    }

}
