package com.firewolf.pattern.composite.standard_optimize;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/5/18 11:17 下午
 */
public class TextFile extends AbstractFile {

    public TextFile(String name) {
        super(name);
    }


    @Override
    public void display() {
        System.out.println("文本文件：" + getName());
    }
}
