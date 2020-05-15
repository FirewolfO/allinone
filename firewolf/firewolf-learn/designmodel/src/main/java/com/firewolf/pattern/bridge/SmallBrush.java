package com.firewolf.pattern.bridge;

/**
 * 描述：抽象部分具体实现
 * Author：liuxing
 * Date：2020/5/15
 */
public class SmallBrush extends Brush {
    @Override
    public String draw() {
        return "小号画笔，颜色是：" + color.getColor();
    }
}
