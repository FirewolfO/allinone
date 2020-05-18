package com.firewolf.pattern.composite.standard;

/**
 * Description: 抽象构建
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/5/18 11:08 下午
 */
public abstract class AbstractFile {
    private String name;
    public AbstractFile(String name) {
        this.name = name;
    }
    // 添加，容器组件才有的方法
    public abstract void add(AbstractFile abstractFile);
    //删除，容器组件才有的方法
    public abstract void remove(AbstractFile abstractFile);
    // 获取名字，容器组件和叶子组件都有的方法
    public abstract void display();

    public String getName() {
        return name;
    }
}
