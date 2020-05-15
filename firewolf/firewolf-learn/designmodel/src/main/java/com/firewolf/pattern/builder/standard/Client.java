package com.firewolf.pattern.builder.standard;

import com.firewolf.pattern.builder.Computer;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/5/12 11:24 下午
 */
public class Client {

    public static void main(String[] args) {
        // 可以通过配置文件的方式灵活更改Builder
        ComputerBuilder builder = new LOLComputerBuilder();

        ComputerDirector director = new ComputerDirector(builder);

        Computer computer = director.buildComputer();

        System.out.println(computer);

    }
}
