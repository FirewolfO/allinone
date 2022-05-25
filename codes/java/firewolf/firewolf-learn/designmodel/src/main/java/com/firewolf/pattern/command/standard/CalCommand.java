package com.firewolf.pattern.command.standard;

/**
 * 描述：抽象Command
 * Author：liuxing
 * Date：2020/7/9
 */
public interface CalCommand {

    /**
     * 计算操作
     *
     * @param value
     * @return
     */
    int cal(int value);

    /**
     * 撤销操作
     *
     * @return
     */
    int undo();
}
