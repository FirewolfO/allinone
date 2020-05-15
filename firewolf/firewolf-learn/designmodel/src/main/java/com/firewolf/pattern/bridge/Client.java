package com.firewolf.pattern.bridge;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/5/15
 */
public class Client {

    public static void main(String[] args) {
        Brush brush = new BigBrush();
        brush.setColor(new YelloColor());
        System.out.println(brush.draw());
    }
}
