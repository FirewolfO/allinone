package com.firewolf.pattern.flyweight;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/7/6
 */
public class Client {
    public static void main(String[] args) {
        IgoChessman black = IgoChessmanFactory.getIgoChessman("black");

        // 这里传入外部状态
        black.display(new Position(12,3));
    }
}
