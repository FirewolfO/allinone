package com.firewolf.pattern.facade;

/**
 * Description: 业务子系统
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/7/4 8:28 下午
 */
public class FileWriter {
    public void writeFile(String content, String destFileName) {
        System.out.println("write " + content + " to " + destFileName);
    }
}
