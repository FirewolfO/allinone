package com.firewolf.pattern.builder.whole;

import com.firewolf.pattern.builder.Computer;

/**
 * Description: 指挥者
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/5/12 11:21 下午
 */
public class ComputerDirector {

    // 引入构建者
    private ComputerBuilder computerBuilder;

    public ComputerDirector(ComputerBuilder computerBuilder) {
        this.computerBuilder = computerBuilder;
    }

    // 构建出产品
    public Computer buildComputer() {
        computerBuilder.buildCpu();
        computerBuilder.buildDisplayer();
        computerBuilder.buildHardDisk();
        computerBuilder.buildMemory();
        return computerBuilder.getResult();
    }


    public ComputerBuilder getComputerBuilder() {
        return computerBuilder;
    }

    public void setComputerBuilder(ComputerBuilder computerBuilder) {
        this.computerBuilder = computerBuilder;
    }
}
