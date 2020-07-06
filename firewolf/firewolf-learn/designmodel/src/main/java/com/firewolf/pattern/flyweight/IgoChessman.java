package com.firewolf.pattern.flyweight;

/**
 * 描述：棋子，抽象享元类
 * Author：liuxing
 * Date：2020/7/6
 */
public abstract class IgoChessman {
    public abstract String getColor();

    /**
     * 操作方法，可能会传入外部状态属性，如这里的position
     *
     * @param position
     */
    public void display(Position position) {
        System.out.println("棋子颜色：" + getColor() + "，棋子位置：x=" + position.getX() + ",y=" + position.getY());
    }
}
