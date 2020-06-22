package com.firewolf.testcase.junit5.s15_migration;

import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.Timeout;

import java.util.concurrent.TimeUnit;

/**
 * 描述：使用junit4中的@Ignore，需要引入junit-jupiter-migrationsupport依赖，下面类上面的注解可以不加
 * Author：liuxing
 * Date：2020/6/22
 */
@EnableRuleMigrationSupport
public class RuleTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Rule
    public Timeout timeout = new Timeout(1, TimeUnit.SECONDS);

    @Test
    public void test() {
        System.out.println(temporaryFolder.getRoot());
    }

    @Test
    public void test2() throws Exception {
        Thread.sleep(2000);

    }
}
