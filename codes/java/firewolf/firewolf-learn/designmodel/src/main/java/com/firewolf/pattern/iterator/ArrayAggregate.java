package com.firewolf.pattern.iterator;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/7/15
 */
public class ArrayAggregate<E> implements Aggregate<E> {

    private Object[] elements = new Object[10];

    @Override
    public Iterator<E> createIterator() {
        return new ArrayIterator<>(this);
    }

    public Object[] getElements() {
        return elements;
    }
}
