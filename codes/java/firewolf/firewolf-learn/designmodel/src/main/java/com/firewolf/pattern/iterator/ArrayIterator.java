package com.firewolf.pattern.iterator;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/7/15
 */
public class ArrayIterator<T> implements Iterator<T> {


    private ArrayAggregate<T> arrayAggregate;

    private Object[] elements;
    private int position = -1;
    private int elementCount = 0;

    public ArrayIterator(ArrayAggregate<T> arrayAggregate) {
        this.arrayAggregate = arrayAggregate;
        this.elements = arrayAggregate.getElements();
    }

    @Override
    public void add(T t) {
        if (elementCount >= elements.length) {
            throw new RuntimeException("数组已经满了....");
        }
        elements[elementCount++] = t;
    }

    @Override
    public T next() {
        return (T) elements[++position];
    }

    @Override
    public boolean hasNext() {
        return position < elementCount - 1;
    }

    @Override
    public T first() {
        return elementCount == 0 ? null : (T) elements[0];
    }

    @Override
    public T last() {
        return elementCount == 0 ? null : (T) elements[elementCount - 1];
    }

    @Override
    public String disPlay() {
        String r = "[";
        StringBuilder result = new StringBuilder("");
        if (elementCount > 0) {
            for (int i = 0; i < elementCount; i++) {
                result.append(elements[i].toString()).append(",");
            }
            r = r + result.substring(0, result.length() - 1);
        }

        return r + "]";
    }
}
