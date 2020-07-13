package com.firewolf.pattern.command.standard;

/**
 * 描述：客户端
 * Author：liuxing
 * Date：2020/7/9
 */
public class Client {
    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.setCommand(new AddCommand());
        System.out.println(calculator.execute(10));
        System.out.println(calculator.undo());
        System.out.println(calculator.execute(3));
        System.out.println(calculator.execute(5));
        System.out.println(calculator.undo());
    }
}