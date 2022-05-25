package com.firewolf.pattern.bridge;

import lombok.Data;

/**
 * 描述：画笔，充当抽象部分的公共抽象
 * Author：liuxing
 * Date：2020/5/15
 */
@Data
public abstract class Brush {
    protected Color color;

    public abstract String draw();
}
