package com.firewolf.testcase.juint4;


import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Description: Matcher
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/6/1 11:54 下午
 */
public class MatcherTest {

    @Test
    public void test1() {
        String s = new String("Hello,World");

        // 不需要匹配器的用法
        assertThat("字符串长度小于5", s.length() > 5);

        // 用于对象的
        // is(T) / equalTo(T)：判断内容是否一样，调用了equals
        assertThat(s, is(new String("Hello,World")));
        assertThat(s, equalTo("Hello,World"));

        // sameInstance / theInstance(T) ：是某个对象，==判断
        assertThat(s, theInstance(s));
        assertThat(s, sameInstance(s));

        // nullValue：空值
        Object a = null;
        assertThat(null, nullValue());
        assertThat(null, nullValue(String.class));

        // notNullValue: 非空值
        assertThat(s, notNullValue(String.class));
        assertThat(s, notNullValue());

        // not(T) : 不等于，equals
        assertThat(s, not("hello"));

        // isA(Class<T>) / instanceOf(Class<T>) / any(Class<T>)：是什么类型的
        assertThat(s, isA(String.class));
        assertThat(s, instanceOf(String.class));


        // 字符串相关
        // startsWith / endsWith(String)：某个字符串是什么开头/结尾的
        assertThat(s, endsWith("ld"));
        // containsString(String)：包含某个字符串
        assertThat(s, containsString(","));


        // 集合
        List<String> sList = Arrays.asList("aa", "bb", "ccc");
        // hasItems(T...)：集合中是否有指定的所有元素
        assertThat(sList, hasItems("aa", "bb"));
        // 要求找到能匹配上所有的匹配器的元素
        // hasItems(Matcher<? super T>...)：是否有元素能匹配所有的匹配器
        assertThat(sList, hasItems(isA(String.class), new StringLengthMatcher(2)));
        // hasItem(T): 是否有某个元素
        // hasItem(Matcher<? super T>)：是否有匹配某个匹配器的元素
        //everyItem(Matcher<? super T>):是否所有元素都满足要求
        assertThat(sList, everyItem(new StringLengthMatcher(1)));


        // 多个匹配器组合
        // either ：是否能满组其中一个匹配器，只能使用or连接
        assertThat(s, either(containsString("Hello")).or(containsString("111")));

        // both：用来连接多个匹配器，之间可以使用and和or来连接，如果是and，要求都匹配，如果是or，要求某个匹配
        assertThat(s, both(containsString("Hello")).and(containsString("World")).and(containsString(",")).or(containsString("aaa")));

        // anyOf(Matcher<? super T>...)：是否满足其中一个匹配器
        assertThat(s, anyOf(containsString("Hello"), containsString("BBB")));

        // allOf(Matcher<? super T>...)：是否满足所有匹配器
        assertThat(s, allOf(containsString("Hello"), containsString("World")));

    }

    /**
     * 自定义Matcher
     */
    class StringLengthMatcher implements Matcher<String> {

        private int length;

        public StringLengthMatcher(int length) {
            this.length = length;
        }

        @Override
        public boolean matches(Object item) {
            return ((String) item).length() > length;
        }

        @Override
        public void describeMismatch(Object item, Description mismatchDescription) {
        }

        @Override
        public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
        }

        @Override
        public void describeTo(Description description) {
        }
    }

    class StringLengthMatcher2 extends BaseMatcher<String> {
        private int length;

        public StringLengthMatcher2(int length) {
            this.length = length;
        }

        @Override
        public boolean matches(Object item) {
            return ((String) item).length() > length;
        }

        @Override
        public void describeTo(Description description) {

        }
    }
}
