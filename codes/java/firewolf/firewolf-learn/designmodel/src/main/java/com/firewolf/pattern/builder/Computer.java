package com.firewolf.pattern.builder;

/**
 * Description: 充当商品类 Product，其中的各个字段是各个组成部分
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/5/12 11:07 下午
 */
public class Computer {

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


    /**
     * 显示器
     */
    private String displayer;

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getHardDisk() {
        return hardDisk;
    }

    public void setHardDisk(String hardDisk) {
        this.hardDisk = hardDisk;
    }

    public String getDisplayer() {
        return displayer;
    }

    public void setDisplayer(String displayer) {
        this.displayer = displayer;
    }

    @Override
    public String toString() {
        return "Computer{" +
                "cpu='" + cpu + '\'' +
                ", memory='" + memory + '\'' +
                ", hardDisk='" + hardDisk + '\'' +
                ", displayer='" + displayer + '\'' +
                '}';
    }
}
