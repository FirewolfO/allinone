package 其他;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2021/3/30 11:27 上午
 */
public class ValidSoudu {

    public boolean isValidSudoku(char[][] board) {
        Map<Character, Integer>[] row = new HashMap[9];
        Map<Character, Integer>[] col = new HashMap[9];
        Map<Character, Integer>[] box = new HashMap[9];
        for (int i = 0; i < 9; i++) {
            row[i] = new HashMap<>();
            col[i] = new HashMap<>();
            box[i] = new HashMap<>();
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                char c = board[i][j];
                if (c != '.') {
                    int box_index = (i / 3) * 3 + j / 3;
                    row[i].put(c, row[i].getOrDefault(c, 0) + 1);
                    col[j].put(c, col[j].getOrDefault(c, 0) + 1);
                    box[box_index].put(c, box[box_index].getOrDefault(c, 0) + 1);
                    if (row[i].get(c) > 1 || col[j].get(c) > 1
                            || box[box_index].get(c) > 1) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public static void main(String[] args) {

    }
}
