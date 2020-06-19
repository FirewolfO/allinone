package com.firewolf.testcase.junit5.s03_displayname;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/6/19
 */
@DisplayNameGeneration(MyDisplayNameGenerator.class)
public class DisplayNameGeneratorTest {

    @Test
    public void testAAA() {
        System.out.println("AAA");
    }

    @Nested
    class A {
        @Test
        public void testA_123() {
            System.out.println("testA_123");
        }

        @Test
        public void testB_123() {
            System.out.println("testB_123");
        }
    }
}

class MyDisplayNameGenerator implements DisplayNameGenerator {

    /**
     * 类名
     *
     * @param testClass
     * @return
     */
    @Override
    public String generateDisplayNameForClass(Class<?> testClass) {
        return "这是一个测试类：" + testClass.getSimpleName();
    }

    /**
     * 内部测试类的名字
     *
     * @param nestedClass
     * @return
     */
    @Override
    public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
        return "这是内部测试类：" + nestedClass.getSimpleName();
    }

    /**
     * 各个方法的名字
     *
     * @param testClass
     * @param testMethod
     * @return
     */
    @Override
    public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
        return "这是" + testClass.getSimpleName() + "类的" + testMethod.getName() + "测试方法";
    }
}
