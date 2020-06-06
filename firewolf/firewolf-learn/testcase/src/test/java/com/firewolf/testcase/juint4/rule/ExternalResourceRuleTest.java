package com.firewolf.testcase.juint4.rule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import java.io.File;

/**
 * 描述：用于在测试用例开始之前创建外部资源，如：文件、Socket连接、服务器、数据库连接等等，如
 * Author：liuxing
 * Date：2020/6/5
 */
public class ExternalResourceRuleTest {

    private File file;

    @Rule
    public ExternalResource resource = new ExternalResource() {
        @Override
        protected void before() throws Throwable {
            file = File.createTempFile("test", "txt");
        }

        @Override
        protected void after() {
            file.delete();
        }
    };


    @Test
    public void testFile(){
        System.out.println(file.getAbsolutePath());
    }
}
