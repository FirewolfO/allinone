package com.firewolf.testcase.junit5.s15_extentions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/6/22
 */
@ExtendWith(MyTestWatcherExtension.class)
@ExtendWith(IgnoreExceptionExtentsion.class)
public class ExtensionTest {


    @TempDir
    static Path tmp1;

    @Test
    void test1() {
        System.out.println(tmp1.getRoot());
    }

    @Test
    void test2(@TempDir Path tmp2) {
        System.out.println(tmp2.getRoot());
    }

    @Test
    void test3(){
        int a = 1/0;
    }


}
