package com.firewolf.pattern.command.multi.command;

/**
 * 描述：抽象Command
 * Author：liuxing
 * Date：2020/7/13
 */
public interface CalCommand {

    int execute(int firstValue, int secondValue);

    int undo(int lastValue, int undoValue);
}
