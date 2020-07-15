package com.firewolf.pattern.iterator;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/7/15
 */
public interface Iterator<T> {

    void add(T t);

    T next();

    boolean hasNext();

    T first();

    T last();

    String disPlay();
}
