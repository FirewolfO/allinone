package com.megvii.ebg.pangu.ms.base.provider.setup;

import com.firewolf.learn.test.Logger;
import org.junit.BeforeClass;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/4/26
 */
public class AllContainers {

    private static Logger log = new Logger();

    private static TestContainer allContainters = TestContainerHelper.getAllContainters();

    @BeforeClass
    public static void setUp() {
        allContainters.start();
    }

    //    @AfterClass 不可以停止，停止会报错
    public static void tearDown() {
        log.info("close containers !!!! ");
        allContainters.stop();
    }

}
