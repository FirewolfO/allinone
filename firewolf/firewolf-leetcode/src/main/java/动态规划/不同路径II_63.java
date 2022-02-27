package 动态规划;

import utils.ArrayUtils;

class 不同路径II_63 {

    public static void main(String[] args) {
        int[][] obstacleGrid = ArrayUtils.to2Array("[[0,0,0],[0,1,0],[0,0,0]]", ",");
        int i = new 不同路径II_63().uniquePathsWithObstacles(obstacleGrid);
        System.out.println(i);
    }

    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;
        int[][] dp = new int[m][n];
        // 第一行都是只有一条路径
        for (int i = 0; i < m && obstacleGrid[i][0] == 0; i++) { // 第一列，从上往下，遇到障碍物之前都是一条路径，往后都走不通，为0
            dp[i][0] = 1;
        }
        // 第一列只有一条路径
        for (int i = 0; i < n && obstacleGrid[0][i] == 0; i++) { //第一行，从左往右，遇到障碍物之前都是一条路径，往后都走不通，为0
            dp[0][i] = 1;
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (obstacleGrid[i][j] == 0) { // 如果空位置，计算，否则为0
                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1]; // 从上方到或者是从左边到
                }
            }
        }
        return dp[m - 1][n - 1];
    }
}