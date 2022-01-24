package 回溯;

import java.util.Arrays;

class Solution {

    public static void main(String[] args) {
        char[][] board = new char[9][9];
        board[0] = new char[]{'5', '3', '.', '.', '7', '.', '.', '.', '.'};
        board[1] = new char[]{'6', '.', '.', '1', '9', '5', '.', '.', '.'};
        board[2] = new char[]{'.', '9', '8', '.', '.', '.', '.', '6', '.'};
        board[3] = new char[]{'8', '.', '.', '.', '6', '.', '.', '.', '3'};
        board[4] = new char[]{'4', '.', '.', '8', '.', '3', '.', '.', '1'};
        board[5] = new char[]{'7', '.', '.', '.', '2', '.', '.', '.', '6'};
        board[6] = new char[]{'.', '6', '.', '.', '.', '.', '2', '8', '.'};
        board[7] = new char[]{'.', '.', '.', '4', '1', '9', '.', '.', '5'};
        board[8] = new char[]{'.', '.', '.', '.', '8', '.', '.', '7', '9'};
        new Solution().solveSudoku(board);
        for (int i = 0; i < 9; i++) {
            System.out.println(Arrays.toString(board[i]));
        }
    }

    public void solveSudoku(char[][] board) {
        solveSudokuHelper(board, 0, 0);
    }

    private boolean solveSudokuHelper(char[][] board, int row, int col) {
        if (row == 9 && col == 0) { // 所有的行和列都处理完，结束
            return true;
        }
        if (board[row][col] == '.') {
            for (int i = 1; i <= 9; i++) { // 尝试将空位置放上 1~9看看是否满足
                if (isValid(board, row, col, i)) {
                    board[row][col] = (char) (i + '0');
                    if (solveNext(board, row, col)) return true;
                    board[row][col] = '.'; // 回溯
                }
            }
        } else {//已经是数字了，解决下一个
            if (solveNext(board, row, col)) return true;
        }
        return false;
    }

    /**
     * 解决下一个位置元素
     *
     * @param board
     * @param row
     * @param col
     * @return
     */
    private boolean solveNext(char[][] board, int row, int col) {
        if (col < 8) { // 如果 col <8 ，处理同行下一列
            if (solveSudokuHelper(board, row, col + 1)) return true;
        } else { // == 8了，进入下一行第一个
            if (solveSudokuHelper(board, row + 1, 0)) return true;
        }
        return false;
    }

    private boolean isValid(char[][] board, int row, int col, int n) { // 判断board[row][col] 上是否可以放入n
        char c = (char) (n + '0');
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == c) return false; //同行是否有
            if (board[i][col] == c) return false; // 同列是否有
        }
        for (int i = row / 3 * 3; i < (row / 3 + 1) * 3; i++) {
            for (int j = col / 3 * 3; j < (col / 3 + 1) * 3; j++) { // 同一个正方体区间
                if (board[i][j] == c) return false;
            }
        }
        return true;
    }
}