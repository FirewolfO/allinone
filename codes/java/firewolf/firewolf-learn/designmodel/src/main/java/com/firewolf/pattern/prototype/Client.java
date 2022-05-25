package com.firewolf.pattern.prototype;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/5/9
 */
public class Client {

    public static void main(String[] args) {
        Dog dog = new Dog();
        dog.setColor("black");

        Son son = new Son();
        son.setName("jim");
        son.setDog(dog);

        Person person = new Person();
        person.setName("tom");
        person.setSon(son);

        Person person2 = person.clone();
        // 打印true，因为地址一样
        System.out.println(person.getSon() == person2.getSon());
        // 打印true，因为地址一样
        System.out.println(person2.getSon().getDog() == person.getSon().getDog());


        Person person3 = person.deepClone1();
        // 打印false，因为地址不同
        System.out.println(person3.getSon() == person.getSon());
        // 打印false，因为地址不同
        System.out.println(person3.getSon().getDog() == person.getSon().getDog());

    }
}
