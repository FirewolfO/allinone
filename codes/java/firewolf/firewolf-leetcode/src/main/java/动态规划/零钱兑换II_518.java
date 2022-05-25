package 动态规划;

import utils.ArrayUtils;

class 零钱兑换II_518 {

    public static void main(String[] args) {
        int[] coins = ArrayUtils.toArray("[1, 2, 5]", ",");
        System.out.println(new 零钱兑换II_518().change(5, coins));
        System.out.println(new 零钱兑换II_518().change2(5, coins));
    }

    public int change(int amount, int[] coins) {

        //dp[x] 表示金额之和等于 xx 的硬币组合数，目标是求 dp[amount]。
        int[] dp = new int[amount + 1];
        dp[0] = 1;  // 有当不选取任何硬币时，金额之和才为 0，因此只有 1 种硬币组合。
        for (int i = 0; i < coins.length; i++) {
            for (int j = coins[i]; j <= amount; j++) {
                dp[j] += dp[j - coins[i]];
            }
        }
        return dp[amount];
    }


    public int change2(int amount, int[] coins) {
        // dp[i][j]： coins[i]~coins[i] 和为j的组合个数
        int[][] dp = new int[coins.length + 1][amount + 1];
        dp[0][0] = 1;
        for (int i = 1; i <= coins.length; i++) {
            for (int j = 0; j <= amount; j++) {
                // 不使用coins[i]的组合数，   任意个coins[i]硬币
                dp[i][j] = dp[i - 1][j];
                for (int k = 1; k * coins[i - 1] <= j; k++) {
                    dp[i][j] += dp[i - 1][j - k * coins[i - 1]];
                }
            }
        }
        return dp[coins.length][amount];
    }
}