package 动态规划;

import utils.ArrayUtils;

class 组合总和Ⅳ_377 {


    public static void main(String[] args) {
        int[] ints = ArrayUtils.toArray("[1,2,3]", ",");
        int i = new 组合总和Ⅳ_377().combinationSum4Other(ints, 4);
        System.out.println(i);
    }

    public int combinationSum4(int[] nums, int target) {
        int dp[] = new int[target + 1];
        dp[0] = 1;
        for (int i = 0; i <= target; i++) {
            for (int j = 0; j < nums.length; j++) {
                if (nums[j] <= i) {
                    dp[i] += dp[i - nums[j]];
                }
            }
        }
        return dp[target];
    }

    public int combinationSum4Other(int[] nums, int target) {
        int dp[][] = new int[nums.length + 1][target + 1];
        dp[0][0] = 1;
        for (int i = 0; i <= target; i++) {
            for (int j = 1; j < nums.length + 1; j++) {
                dp[j][i] = dp[j - 1][i];
                for (int k = 1; k * nums[j - 1] <= i; k++) {
                    dp[j][i] += dp[j - 1][i - k * nums[j - 1]];
                }
            }
        }
        return dp[nums.length][target];
    }
}