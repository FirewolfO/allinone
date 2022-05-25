package com.firewolf.testcase.junit5.s13_parameterized;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 描述：参数化测试
 * Author：liuxing
 * Date：2020/6/21
 */
public class ParameterizedTestDriver {

    /**
     * 传入null 值、空值、数据值
     *
     * @param str
     */
    @ParameterizedTest
    // 传入三个字符串
    @ValueSource(strings = {"haha", "hehe", "heihei"})
    // 传入null和""
    @NullAndEmptySource
    void testValueSourceStringParams(String str) {
        System.out.println("|" + str + "|");
    }

    /**
     * 传入枚举
     *
     * @param unit
     */
    @ParameterizedTest
    @EnumSource(value = ChronoUnit.class, names = {"SECONDS", "DAYS"}, mode = EnumSource.Mode.INCLUDE)
    void testEumSource(ChronoUnit unit) {
        assertNotNull(unit);
    }

    /**
     * 方法生成参数
     *
     * @param chronoUnit
     */
    @ParameterizedTest
    @MethodSource("com.firewolf.testcase.junit5.s13_parameterized.ParameterizedTestDriver#provider")
    void testMethodParams(ChronoUnit chronoUnit) {
        System.out.println(chronoUnit);
    }

    static Stream<ChronoUnit> provider() {
        return Stream.of(ChronoUnit.HALF_DAYS, ChronoUnit.DAYS,ChronoUnit.FOREVER);
    }


    /**
     * 使用csv格式传入参数
     *
     * @param fruit
     * @param rank
     */
    @ParameterizedTest
    @CsvSource(value = {
            "apple        ;       2;      heihei",
            "             ;       1;      heihei",
            "'lemon; lime';       2;      haha",
            "nal;               0xF1;     hehe"
    }, delimiter = ';')
    void testWithCsvSource(String fruit, int rank) {
        System.out.println("fruit=" + fruit + ",rank=" + rank);
        assertNotEquals(0, rank);
    }

    /**
     * 使用csv文件传入参数，要求文件在resource文件夹下面，如果文件中的某项数据包含了分隔符，那么需要使用""来引用起来
     *
     * @param fruit
     * @param rank
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/test.csv", delimiter = ';')
    void testWithCsvFileSource(String fruit, int rank) {
        System.out.println("fruit=" + fruit + ",rank=" + rank);
        assertNotEquals(0, rank);
        assertNotEquals(0, rank);
    }

    /**
     * 使用ArgumentsAccessor对参数进行封装
     *
     * @param arguments
     */
    @ParameterizedTest
    @CsvSource({
            "Jane, Doe, F, 1990-05-20",
            "John, Doe, M, 1990-10-22"
    })
    void testWithArgumentsAccessor(ArgumentsAccessor arguments) {
        Person person = new Person(arguments.getString(0),
                arguments.getString(1),
                arguments.get(2, String.class),
                arguments.get(3, LocalDate.class));

        if (person.getFirstName().equals("Jane")) {
            assertEquals("F", person.getGender());
        } else {
            assertEquals("M", person.getGender());
        }
        assertEquals("Doe", person.getLastName());
        assertEquals(1990, person.getDateOfBirth().getYear());
    }


    /**
     * 自定义ArgumentsAccessor
     *
     * @param person
     */
    @ParameterizedTest
    @CsvSource({
            "Jane, Doe, F, 1990-05-20",
            "John, Doe, M, 1990-10-22"
    })
    void testWithArgumentsAccessor2(@AggregateWith(PersonArgumentsAccessor.class) Person person) {
        if (person.getFirstName().equals("Jane")) {
            assertEquals("F", person.getGender());
        } else {
            assertEquals("M", person.getGender());
        }
        assertEquals("Doe", person.getLastName());
        assertEquals(1990, person.getDateOfBirth().getYear());
    }

}

class PersonArgumentsAccessor implements ArgumentsAggregator {
    @Override
    public Person aggregateArguments(ArgumentsAccessor arguments, ParameterContext parameterContext) throws ArgumentsAggregationException {
        Person person = new Person(arguments.getString(0),
                arguments.getString(1),
                arguments.get(2, String.class),
                arguments.get(3, LocalDate.class));

        return person;
    }
}



