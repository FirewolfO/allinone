package com.firewolf.pattern.memento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/7/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChessmanMemento {

    /**
     * 棋子的X坐标
     */
    private int x;
    /**
     * 棋子的Y坐标
     */
    private int y;

}
