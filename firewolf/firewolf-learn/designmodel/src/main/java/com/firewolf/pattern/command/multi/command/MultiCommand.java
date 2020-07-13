package com.firewolf.pattern.command.multi.command;

import com.firewolf.pattern.command.multi.receiver.Multier;

/**
 * 描述：乘命令，具体命令类
 * Author：liuxing
 * Date：2020/7/13
 */
public class MultiCommand implements CalCommand {

    private Multier multier = new Multier();

    @Override
    public int execute(int firstValue, int secondValue) {
        return multier.mult(firstValue, secondValue);
    }

    @Override
    public int undo(int lastValue, int undoValue) {
        return multier.undo(lastValue, undoValue);
    }
}
