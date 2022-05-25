package com.firewolf.pattern.composite.safe;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/5/18 11:17 下午
 */
public class ImageFile extends AbstractFile {

    public ImageFile(String name) {
        super(name);
    }

    @Override
    public void display() {
        System.out.println("图片文件：" + getName());
    }
}
