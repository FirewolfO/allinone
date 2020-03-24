package com.firewolf.busi.example.simple;

import com.firewolf.busi.example.junit.Person;
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

public class ParamTestDriver {

    /**
     * 通过@ValueSource注解来注入一组参数,会一个一个的传入进去
     *
     * @param str
     */
    @ParameterizedTest
    // 传入三个字符串
    @ValueSource(strings = {"haha", "hehe", "heihei"})
    // 传入null和""
    @NullAndEmptySource
    void testValueSourceStringParams(String str) {
        assertEquals(str.length(), 4);
    }

    @ParameterizedTest(name = "第 {index}个参数， 当前参数：{arguments}")
    @ValueSource(ints = {1, 2, 3})
    void testValueSourceIntParams(int param) {
        assertTrue(param > 0 && param < 4);
    }

    /**
     * 传入枚举参数
     *
     * @param unit
     */
    @ParameterizedTest
    @EnumSource(value = ChronoUnit.class, names = {"SECONDS", "DAYS"}, mode = EnumSource.Mode.INCLUDE)
    void testEumSource(ChronoUnit unit) {
        assertNotNull(unit);
    }


    /**
     * 通过静态方法来传入参数，要求这个方法返回是Stream
     *
     * @param chronoUnit
     */
    @ParameterizedTest
    @MethodSource("com.firewolf.busi.example.ParamTestDriver#provider")
    void testMethodParams(ChronoUnit chronoUnit) {
        System.out.println(chronoUnit);
    }

    static Stream<ChronoUnit> provider() {
        return Stream.of(ChronoUnit.HALF_DAYS, ChronoUnit.DAYS);
    }

    /**
     * 通过Csv格式传入参数，如果每一项的数据超过方法参数的个数，那么后面的会被丢弃
     * - 如果某项数据里面包含了分隔符，那么需要使用''括起来
     * - 需要想传入null，那么这一项不写即可
     * - 如果数据项多于参数，那么后面的会被丢弃
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
        System.out.println(fruit);
        assertNotNull(fruit);
        assertNotEquals(0, rank);
    }


    @ParameterizedTest
    @CsvFileSource(resources = "/test.csv", delimiter = ';')
    void testWithCsvFileSource(String fruit, int rank) {
        System.out.println(fruit);
        assertNotNull(fruit);
        assertNotEquals(0, rank);
    }


    /**
     * 把参数转成我们需要的类型
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
}
