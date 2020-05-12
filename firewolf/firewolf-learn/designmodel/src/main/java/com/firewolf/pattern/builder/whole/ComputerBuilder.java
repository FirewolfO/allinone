package com.firewolf.pattern.builder.whole;

import com.firewolf.pattern.builder.Computer;

/**
 * Description: 构建者抽象类
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/5/12 11:13 下午
 */
public abstract class ComputerBuilder {

    protected Computer computer = new Computer();


    // 下面是各个组成部分的构建方法
    public abstract void buildCpu();

    public abstract void buildHardDisk();

    public abstract void buildMemory();

    public abstract void buildDisplayer();

    // 返回构建的结果
    public Computer getResult() {
        return computer;
    }
}
