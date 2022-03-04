package 动态规划;

import utils.ArrayUtils;

class 下降路径最小和II_1289 {
    public static void main(String[] args) {
        int[][] ints = ArrayUtils.to2Array("[[1,2,3],[4,5,6],[7,8,9]]", ",");
        System.out.println(new 下降路径最小和II_1289().minFallingPathSum(ints));
    }

    public int minFallingPathSum(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        //dp[i][j]：达到i,j的最小路径和
        int[][] dp = new int[m][n];
        for (int j = 0; j < n; j++) {
            dp[0][j] = grid[0][j];
        }
        for (int i = 1; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int min = Integer.MAX_VALUE;
                for (int k = 0; k < n; k++) {
                    if (k != j) {
                        min = Math.min(min, dp[i - 1][k]); // 找不在同一列的
                    }
                }
                dp[i][j] = min + grid[i][j];
            }
        }
        int res = Integer.MAX_VALUE;
        for (int j = 0; j < n; j++) {
            res = Integer.min(res, dp[m - 1][j]);
        }
        return res;
    }
}