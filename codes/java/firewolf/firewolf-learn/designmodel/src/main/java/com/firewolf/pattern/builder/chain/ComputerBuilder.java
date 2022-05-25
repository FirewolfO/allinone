package com.firewolf.pattern.builder.chain;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/7/2
 */
public class ComputerBuilder {

    /**
     * cpu
     */
    private String cpu;

    /**
     * 内存
     */
    private String memory;


    /**
     * 硬盘
     */
    private String hardDisk;

    public ComputerBuilder buildCpu(String cpu){
        this.cpu = cpu;
        return this;
    }

    public ComputerBuilder buildMemory(String memory){
        this.memory = memory;
        return this;
    }

    public ComputerBuilder buildeHardDisk(String hardDisk){
        this.hardDisk = hardDisk;
        return this;
    }

}
