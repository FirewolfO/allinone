package com.firewolf.pattern.memento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * 描述：象棋棋子类，原发器
 * Author：liuxing
 * Date：2020/7/16
 */
@Data
@AllArgsConstructor
@ToString
public class Chessman {

    /**
     * 棋子名
     */
    private String name;

    /**
     * 棋子的X坐标
     */
    private int x;
    /**
     * 棋子的Y坐标
     */
    private int y;


    /**
     * 保存状态
     *
     * @return
     */
    public ChessmanMemento save() {
        return new ChessmanMemento(x, y);
    }

    public void restore(ChessmanMemento memento) {
        this.x = memento.getX();
        this.y = memento.getY();
    }
}
