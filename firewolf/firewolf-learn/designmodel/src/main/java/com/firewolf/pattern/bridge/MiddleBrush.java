package com.firewolf.pattern.bridge;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/5/15
 */
public class MiddleBrush extends Brush {
    @Override
    public String draw() {
        return "中号画笔，颜色是：" + color.getColor();
    }
}
