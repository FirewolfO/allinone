package com.firewolf.pattern.command.multi.command;

import com.firewolf.pattern.command.multi.receiver.Adder;

/**
 * 描述：加命令，具体命令类
 * Author：liuxing
 * Date：2020/7/13
 */
public class AddCommand implements CalCommand {

    private Adder adder = new Adder();

    @Override
    public int execute(int firstValue, int secondValue) {
        return adder.add(firstValue, secondValue);
    }

    @Override
    public int undo(int lastValue, int undoValue) {
        return adder.undo(lastValue, undoValue);
    }
}
