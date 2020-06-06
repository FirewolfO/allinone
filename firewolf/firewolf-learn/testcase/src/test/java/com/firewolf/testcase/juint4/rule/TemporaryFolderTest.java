package com.firewolf.testcase.juint4.rule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

/**
 * 描述：零食文件夹Rule，这会帮我们创建一个临时目录
 * Author：liuxing
 * Date：2020/6/5
 */
public class TemporaryFolderTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void testTempararyFolder() throws Exception {
        File file = temporaryFolder.newFile("aaa.txt");
        System.out.println(temporaryFolder.getRoot());
        System.out.println(file.getAbsolutePath());
    }
}
