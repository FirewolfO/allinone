package 回溯;

import java.util.ArrayList;
import java.util.List;

class N皇后_51 {

    public static void main(String[] args) {
        System.out.println(new N皇后_51().solveNQueens(4));
    }


    List<List<String>> res = new ArrayList<>();

    public List<List<String>> solveNQueens(int n) {
        char[][] qipan = new char[n][n];
        for (int i = 0; i < qipan.length; i++) {
            for (int j = 0; j < qipan[i].length; j++) {
                qipan[i][j] = '.';
            }
        }
        solveNQueensHelper(qipan, 0, n);
        return res;
    }

    private void solveNQueensHelper(char[][] qipan, int row, int n) {
        if (row == n) {
            List<String> one = new ArrayList<>();
            for (int i = 0; i < qipan.length; i++) {
                StringBuffer sb = new StringBuffer();
                for (int j = 0; j < qipan[i].length; j++) {
                    sb.append(qipan[i][j]);
                }
                one.add(sb.toString());
            }
            res.add(new ArrayList<>(one));
            return;
        }
        for (int col = 0; col < n; col++) {
            if (isValid(qipan, row, col, n)) {
                qipan[row][col] = 'Q';
                solveNQueensHelper(qipan, row + 1, n);
                qipan[row][col] = '.';
            }
        }
    }

    private boolean isValid(char[][] qipan, int row, int col, int n) {
        for (int i = 0; i < row; i++) {
            // 判断行
            // 不同行，不用判断
            // 判断列
            if (qipan[i][col] == 'Q') return false;
            // 判断45度角, row-i == col-x
            if ((col - row + i) >= 0 && ((col - row + i) < n) && qipan[i][col - row + i] == 'Q')
                return false;
            // 判断135度角   row -i == x-col
            if ((row + col - i) >= 0 && ((row + col - i) < n) && qipan[i][row + col - i] == 'Q')
                return false;
        }
        return true;
    }
}