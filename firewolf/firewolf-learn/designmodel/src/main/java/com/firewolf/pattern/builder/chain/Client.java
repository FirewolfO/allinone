package com.firewolf.pattern.builder.chain;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/7/2
 */
public class Client {

    public static void main(String[] args) {
        ComputerBuilder computerBuilder = new ComputerBuilder().buildCpu("inter i7").buildeHardDisk("SSD").buildMemory("32G");
        System.out.println(computerBuilder);
    }
}
