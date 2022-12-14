package com.firewolf.pattern.builder.simplify;

import com.firewolf.pattern.builder.Computer;

/**
 * Description: 构建者和指挥者合二为一
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/5/12 11:59 下午
 */
public abstract class ComputerDirectorBuilder {

    // 下面是各个组成部分的构建方法
    public abstract void buildCpu();

    public abstract void buildHardDisk();

    public abstract void buildMemory();

    public abstract void buildDisplayer();

    public abstract Computer getResult();

    // 构建并返回结果
    public final Computer construct() {
        this.buildCpu();
        this.buildDisplayer();
        this.buildHardDisk();
        this.buildMemory();
        return this.getResult();
    }

}
