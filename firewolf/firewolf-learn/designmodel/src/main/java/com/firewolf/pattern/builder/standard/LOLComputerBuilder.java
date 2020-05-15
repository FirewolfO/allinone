package com.firewolf.pattern.builder.standard;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/5/12 11:16 下午
 */
public class LOLComputerBuilder extends ComputerBuilder {

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
}
