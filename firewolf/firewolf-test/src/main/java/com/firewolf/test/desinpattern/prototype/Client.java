package com.firewolf.test.desinpattern.prototype;

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

        System.out.println(person.getSon() == person2.getSon());

    }
}
