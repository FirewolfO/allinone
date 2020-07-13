package com.firewolf.pattern.command.multi;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/7/13
 */
public class Client {

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        int a = calculator.add(3, 4);
        System.out.println(a);
        int b = calculator.multi(a, 5);
        System.out.println(b);
        int undo = calculator.undo();
        System.out.println(undo);
    }
}
