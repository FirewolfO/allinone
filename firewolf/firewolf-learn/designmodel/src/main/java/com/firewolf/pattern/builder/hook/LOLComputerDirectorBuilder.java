package com.firewolf.pattern.builder.hook;

import com.firewolf.pattern.builder.Computer;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/5/13 12:03 上午
 */
public class LOLComputerDirectorBuilder extends ComputerDirectorBuilder {

    protected Computer computer = new Computer();

    @Override
    public void buildCpu() {
        computer.setCpu("Intel i7");
    }

    @Override
    public void buildHardDisk() {
        computer.setHardDisk("SSD 512");
    }

    @Override
    public void buildMemory() {
        computer.setMemory("金士顿32G");
    }

    @Override
    public void buildDisplayer() {
        computer.setDisplayer("三星32英寸");
    }

    /**
     * 覆盖钩子函数
     * @return
     */
    @Override
    public boolean needCpu() {
        return false;
    }

    @Override
    public Computer getResult() {
        return computer;
    }

}
