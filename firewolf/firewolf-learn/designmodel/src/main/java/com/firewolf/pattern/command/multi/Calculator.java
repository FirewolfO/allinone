package com.firewolf.pattern.command.multi;

import com.firewolf.pattern.command.multi.command.AddCommand;
import com.firewolf.pattern.command.multi.command.CalCommand;
import com.firewolf.pattern.command.multi.command.MultiCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/7/13
 */
public class Calculator {

    private Map<String, CalCommand> commands;

    private final String ADD = "add";
    private final String MULTI = "multi";
    private int lastValue, undoValue; // 记录上次数据，用于撤销操作
    private CalCommand lastCommand;

    {
        commands = new HashMap<>();
        commands.put(ADD, new AddCommand());
        commands.put(MULTI, new MultiCommand());
    }

    public int add(int a, int b) {
        return cal(commands.get(ADD), a, b);
    }

    public int multi(int a, int b) {
        return cal(commands.get(MULTI), a, b);
    }

    public int undo() {
        return lastCommand.undo(lastValue, undoValue);
    }


    private int cal(CalCommand command, int firstValue, int secondValue) {
        int result = command.execute(firstValue, secondValue);
        lastValue = result;
        undoValue = secondValue;
        lastCommand = command;
        return result;
    }
}
