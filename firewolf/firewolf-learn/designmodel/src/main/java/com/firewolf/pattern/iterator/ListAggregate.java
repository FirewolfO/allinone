package com.firewolf.pattern.iterator;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/7/15
 */
public class ListAggregate<T> implements Aggregate<T> {

    private List<T> list = new ArrayList<>();

    @Override
    public Iterator<T> createIterator() {
        return new ListIterator(this);
    }

    public List<T> getList() {
        return list;
    }
}
