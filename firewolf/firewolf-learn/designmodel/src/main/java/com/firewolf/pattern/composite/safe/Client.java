package com.firewolf.pattern.composite.safe;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/5/18 11:20 下午
 */
public class Client {
    public static void main(String[] args) {

        Folder folder = new Folder("所有文件");

        ImageFile img1 = new ImageFile("刘兴.jpg");
        ImageFile img2 = new ImageFile("郭智慧.png");
        Folder imgFolder = new Folder("图片");
        imgFolder.add(img1);
        imgFolder.add(img2);

        TextFile txtFile = new TextFile("情书.txt");
        Folder txtFolder = new Folder("情书");
        txtFolder.add(txtFile);

        folder.add(imgFolder);
        folder.add(txtFolder);

        folder.display();

    }
}
