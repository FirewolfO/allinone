package com.firewolf.pattern.iterator;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/7/15
 */
public interface Aggregate<T> {

    Iterator<T> createIterator();

}
