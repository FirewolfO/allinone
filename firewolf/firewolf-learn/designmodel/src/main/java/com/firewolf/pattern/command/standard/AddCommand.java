package com.firewolf.pattern.command.standard;

/**
 * 描述：具体的Command
 * Author：liuxing
 * Date：2020/7/9
 */
public class AddCommand implements CalCommand {

    private Adder adder = new Adder();

    private int lastValue = 0;

    @Override
    public int cal(int value) {
        lastValue = value;
        return adder.add(value);
    }

    @Override
    public int undo() {
        return adder.add(-lastValue);
    }
}
