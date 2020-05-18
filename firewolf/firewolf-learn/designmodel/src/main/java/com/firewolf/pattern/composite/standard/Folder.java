package com.firewolf.pattern.composite.standard;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 容器构建
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/5/18 11:09 下午
 */
public class Folder extends AbstractFile {

    private List<AbstractFile> files = new ArrayList<>();

    public Folder(String name) {
        super(name);
    }

    @Override
    public void add(AbstractFile abstractFile) {
        files.add(abstractFile);
    }

    @Override
    public void remove(AbstractFile abstractFile) {
        files.remove(abstractFile);
    }

    @Override
    public void display() {
        System.out.println("文件夹：" + getName());
        for (AbstractFile file : files) {
            file.display();
        }
    }


}
