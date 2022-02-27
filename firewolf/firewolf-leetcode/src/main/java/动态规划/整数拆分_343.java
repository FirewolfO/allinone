package 动态规划;

class 整数拆分_343 {

    public static void main(String[] args) {
        int i = new 整数拆分_343().integerBreak(10);
        System.out.println(i);
    }

    public int integerBreak(int n) {
        int[] dp = new int[n + 1];
        dp[2] = 1;
        for (int i = 3; i <= n; i++) {
            for (int j = 1; j < i; j++) {
                //拆分成j和(i-j)，以及对(i-j)做了拆分的，取出最大值
                dp[i] = Math.max(dp[i], Math.max(j * (i - j), dp[i - j] * j));
            }
        }
        return dp[n];
    }
}