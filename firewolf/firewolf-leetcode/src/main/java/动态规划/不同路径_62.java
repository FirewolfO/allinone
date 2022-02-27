package 动态规划;

class 不同路径_62 {

    public static void main(String[] args) {
        int i = new 不同路径_62().uniquePaths(3, 7);
        System.out.println(i);
    }

    public int uniquePaths(int m, int n) {
        int[][] dp = new int[m + 1][n + 1];
        // 第一行都是只有一条路径
        for (int i = 1; i < m + 1; i++) {
            dp[i][1] = 1;
        }
        // 第一列只有一条路径
        for (int i = 1; i < n + 1; i++) {
            dp[1][i] = 1;
        }
        for (int i = 2; i < m + 1; i++) {
            for (int j = 2; j < n + 1; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1]; // 从上方到或者是从左边到
            }
        }
        return dp[m][n];
    }
}