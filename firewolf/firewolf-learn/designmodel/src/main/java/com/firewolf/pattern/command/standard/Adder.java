package com.firewolf.pattern.command.standard;

/**
 * 描述：具体的Receiver
 * Author：liuxing
 * Date：2020/7/9
 */
public class Adder {
    private int init = 0;

    public int add(int value) {
        init += value;
        return init;
    }
}
