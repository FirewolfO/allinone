package com.firewolf.pattern.composite.standard;

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

    public void add(AbstractFile abstractFile) {
        throw new RuntimeException("不支持添加");
    }

    public void remove(AbstractFile abstractFile) {
        throw new RuntimeException("不支持删除");
    }

    @Override
    public void display() {
        System.out.println("文本文件：" + getName());
    }
}
