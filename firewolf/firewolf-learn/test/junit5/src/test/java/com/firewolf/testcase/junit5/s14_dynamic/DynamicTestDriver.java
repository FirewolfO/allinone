package com.firewolf.testcase.junit5.s14_dynamic;

import org.junit.Ignore;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 描述：动态测试，通过@TestFactory来生成测试，要求方法返回：单个DynamicNode 或者是DynamicNode的Stream, Collection, Iterable, Iterator
 * Author：liuxing
 * Date：2020/6/22
 */
public class DynamicTestDriver {

    /**
     * 返回的不是DynamicNode，会抛出异常
     *
     * @return
     */
    @TestFactory
    List<String> dynamicTestsWithInvalidReturnType() {
        return Arrays.asList("Hello");
    }


    /**
     * 创建单个测试
     *
     * @return
     */
    @TestFactory
    DynamicTest dynamicNodeSingleTest() {
        int a = 1;
        return dynamicTest("dynamicNodeSingleTest", () -> assertTrue(a == 1));
    }

    /**
     * 一组测试，可以生成Collection、Iterator、Iterable、数组、Stream等类型的都可以
     *
     * @return
     */
    @TestFactory
    Collection<DynamicNode> dynamicTestsFromCollection() {
        return Arrays.asList(
                dynamicTest("1st dynamic test", () -> System.out.println("dynamicTestsFromCollection")),
                dynamicTest("2nd dynamic test", () -> System.out.println("dynamicTestsFromCollection"))
        );
    }

    /**
     * 只有单个测试的测试容器，里面返回一组测试
     * @return
     */
    @TestFactory
    DynamicNode dynamicNodeSingleContainer() {
        return dynamicContainer("palindromes",
                Stream.of("racecar", "radar", "mom", "dad")
                        .map(text -> dynamicTest(text, () -> assertTrue(text.length() > 2))
                        ));
    }

    /**
     * 测试容器，可以理解为二级目录
     *
     * @return
     */
    @TestFactory
    Stream<DynamicNode> dynamicTestsWithContainers() {
        return Stream.of("A", "B", "C")
                .map(input -> dynamicContainer("Container " + input, Stream.of(
                        dynamicTest("not null", () -> assertNotNull(input)),
                        dynamicContainer("properties", Stream.of(
                                dynamicTest("length > 0", () -> assertTrue(input.length() > 0)),
                                dynamicTest("not empty", () -> assertFalse(input.isEmpty()))
                        ))
                )));
    }


}
