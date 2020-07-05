package com.firewolf.pattern.facade;

/**
 * Description: 业务子系统
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/7/4 8:25 下午
 */
public class FileReader {

    public String readFileContent(String fileName){
        System.out.println("read content from file , fileName="+fileName);
        return "content";
    }
}
