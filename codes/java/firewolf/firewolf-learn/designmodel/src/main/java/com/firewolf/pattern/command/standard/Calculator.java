package com.firewolf.pattern.command.standard;

/**
 * 描述：计算器，充当调用者
 * Author：liuxing
 * Date：2020/7/9
 */
public class Calculator {

    /**
     * Invoker中的Command，
     */
    private CalCommand command;

    public void setCommand(CalCommand command) {
        this.command = command;
    }

    public int execute(int value) {
        return command.cal(value);
    }

    public int undo() {
        return command.undo();
    }
}
