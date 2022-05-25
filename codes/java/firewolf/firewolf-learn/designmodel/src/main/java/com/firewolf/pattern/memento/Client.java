package com.firewolf.pattern.memento;

/**
 * 描述： 客户端
 * Author：liuxing
 * Date：2020/7/16
 */
public class Client {
    // 当前是第几步
    private static int index = -1;
    private static ChessmanCaretaker caretaker = new ChessmanCaretaker();

    public static void main(String[] args) {
        Chessman chessman = new Chessman("车", 1, 1);
        play(chessman);

        chessman.setX(5);
        play(chessman);

        undo(chessman, --index);
        play(chessman);


    }

    /**
     * 下棋
     *
     * @param chessman
     */
    private static void play(Chessman chessman) {
        caretaker.setChessmanMemento(chessman.getName(), chessman.save());
        index++;
        System.out.println(chessman);
    }

    /**
     * 悔棋
     *
     * @param chessman
     * @param i
     */
    private static void undo(Chessman chessman, int i) {
        chessman.restore(caretaker.getChessmanMemento(chessman.getName(), i));
    }
}
