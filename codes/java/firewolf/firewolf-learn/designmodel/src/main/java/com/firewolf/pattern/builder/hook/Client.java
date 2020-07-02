package com.firewolf.pattern.builder.hook;

import com.firewolf.pattern.builder.Computer;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/5/13 12:04 上午
 */
public class Client {

    public static void main(String[] args) {
        ComputerDirectorBuilder directorBuilder = new LOLComputerDirectorBuilder();

        Computer computer = directorBuilder.construct();

        System.out.println(computer);

    }
}
