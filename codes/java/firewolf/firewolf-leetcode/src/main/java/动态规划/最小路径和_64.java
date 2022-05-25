package 动态规划;

import utils.ArrayUtils;

class 最小路径和_64 {
    public static void main(String[] args) {
        int[][] grid = ArrayUtils.to2Array("[[1,3,1],[1,5,1],[4,2,1]]", ",");
        System.out.println(new 最小路径和_64().minPathSum(grid));
    }

    public int minPathSum(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] dp = new int[m][n];
        dp[0][0] = grid[0][0];
        int lastMin = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i > 0 && j > 0) {
                    lastMin = Math.min(dp[i - 1][j], dp[i][j - 1]);
                } else if (i > 0) {
                    lastMin = dp[i - 1][j];
                } else if (j > 0) {
                    lastMin = dp[i][j - 1];
                }
                dp[i][j] = lastMin + grid[i][j];
            }
        }
        return dp[m - 1][n - 1];
    }
}