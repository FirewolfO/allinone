package memento;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：负责人，负责存储备忘录
 * Author：liuxing
 * Date：2020/7/16
 */
public class ChessmanCaretaker {

    private Map<String, List<ChessmanMemento>> maps = new HashMap<>();

    /**
     * 根据棋子名和第几步来进行恢复
     *
     * @param name
     * @param i
     * @return
     */
    public ChessmanMemento getChessmanMemento(String name, int i) {
        return maps.get(name).get(i);
    }

    public void setChessmanMemento(String name, ChessmanMemento memento) {
        if (!maps.containsKey(name)) {
            maps.put(name, new ArrayList<>());
        }
        maps.get(name).add(memento);
    }
}
