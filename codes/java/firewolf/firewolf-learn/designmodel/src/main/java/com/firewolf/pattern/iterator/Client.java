package com.firewolf.pattern.iterator;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/7/15
 */
public class Client {

    public static void main(String[] args) {
        Aggregate<String> aggregate = new ListAggregate<>();
        Iterator<String> iterator = aggregate.createIterator();
        iterator.add("111");
        iterator.add("222");
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        System.out.println(iterator.disPlay());


        Aggregate<String> aggregate1 = new ArrayAggregate<>();
        Iterator<String> iterator1 = aggregate1.createIterator();
        iterator1.add("aaa");
        iterator1.add("bbb");
        while (iterator1.hasNext()) {
            System.out.println(iterator1.next());
        }

        System.out.println(iterator1.disPlay());
    }
}
