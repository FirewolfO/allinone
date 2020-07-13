package com.firewolf.pattern.command.multi.receiver;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/7/13
 */
public class Multier {

    public int mult(int firstValue, int secondValue) {
        return firstValue * secondValue;
    }

    public int undo(int lastValue, int undoValue) {
        return lastValue / undoValue;
    }
}
