package com.firewolf.pattern.iterator;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/7/15
 */
public class ListIterator<E> implements Iterator<E> {

    private ListAggregate<E> listAggregate;
    private List<E> list = null;

    private int position = -1;

    public ListIterator(ListAggregate<E> listAggregate) {
        this.listAggregate = listAggregate;
        this.list = listAggregate.getList();
    }

    @Override
    public void add(E e) {
        list.add(e);
    }

    @Override
    public E next() {
        return list.get(++position);
    }

    @Override
    public boolean hasNext() {
        return position < list.size()-1;
    }

    @Override
    public E first() {
        return list.get(0);
    }

    @Override
    public E last() {
        if (list.size() == 0) {
            return null;
        }
        return list.get(list.size() - 1);
    }

    @Override
    public String disPlay() {
        return list.stream().map(x -> x.toString()).collect(Collectors.joining(",", "{", "}"));
    }
}
