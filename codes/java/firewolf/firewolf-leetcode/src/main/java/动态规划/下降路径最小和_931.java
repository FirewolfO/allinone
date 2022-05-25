package 动态规划;

import utils.ArrayUtils;

class 下降路径最小和_931 {
    public static void main(String[] args) {
        int[][] ints = ArrayUtils.to2Array("[[1,2,3],[4,5,6],[7,8,9]]", ",");
        System.out.println(new 下降路径最小和_931().minFallingPathSum(ints));
    }

    public int minFallingPathSum(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        //dp[i][j] ： 到达 i,j 位置的最小和
        int[][] dp = new int[m][n];
        for (int j = 0; j < n; j++) {
            dp[0][j] = matrix[0][j];
        }
        int left;
        int right;
        for (int i = 1; i < m; i++) {
            for (int j = 0; j < n; j++) {
                left = right = Integer.MAX_VALUE;
                if (j > 0) {
                    left = dp[i - 1][j - 1];
                }
                if (j < n - 1) {
                    right = dp[i - 1][j + 1];
                }
                dp[i][j] = Math.min(dp[i - 1][j], Math.min(left, right)) + matrix[i][j]; // 取左上角、右上角、正上方最小的
            }
        }
        int res = Integer.MAX_VALUE;
        for (int j = 0; j < n; j++) {
            res = Math.min(res, dp[m - 1][j]);
        }
        return res;
    }
}