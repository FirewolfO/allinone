package com.firewolf.busi.example;

import com.firewolf.busi.example.junit.HelloUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;


class HelloUtilsTest {

    private HelloUtils helloUtils;


    @BeforeEach
    void setUp() {
        helloUtils = new HelloUtils();
    }

    @AfterEach
    void tearDown() {
        helloUtils = null;
    }


    /**************** 断言 ***************/
    @Test
    void sayHello() {
        assertEquals(helloUtils.sayHello("liuxing"), "hello,liuxing");
    }

    @Test
    void exceptionTest() {
        assertThrows(ArithmeticException.class, () -> {
            int a = 1 / 0;
        });
    }

    @Test
    void timeOutTest() {
        assertTimeout(Duration.ofSeconds(1), () -> Thread.sleep(2000));
    }

    @Test
    void sayHelloWithPrefix() {
        Exception ex = assertThrows(NullPointerException.class, () -> helloUtils.sayHelloWithPrefix(null, "welcome"));

        assertNull(ex.getMessage());

        assertAll("helloWithPrefix",
                () -> assertEquals(helloUtils.sayHelloWithPrefix("liuxing", "hello"), "hello,liuxing"),
                () -> assertThrows(NullPointerException.class, () -> helloUtils.sayHelloWithPrefix(null, "welcome"))
        );
    }


    /************** 第三方jar *****************/
    @Test
    void testThirdLib() {
        assertThat("hello".length(), is(5));

        assertThat("hello", isA(String.class));

        assertThat(Arrays.asList(1, 2, 3), hasItem(1));


    }

    /**************** 假设 ***************/
    @Test
    void testAssumptions() {

        assumeTrue("hello".startsWith("h"));

        assumeFalse(() -> "hello".endsWith("o"), () -> "hello end with o");

        System.setProperty("env", "dev");
        assumingThat(System.getProperty("env") != null, () -> {
            System.out.println("exec test");
            assertEquals(System.getProperty("env").length(), 4);
        });
    }

}