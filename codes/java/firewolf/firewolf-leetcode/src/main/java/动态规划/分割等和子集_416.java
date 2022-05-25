package 动态规划;

import utils.ArrayUtils;

import java.util.Arrays;

class 分割等和子集_416 {

    public static void main(String[] args) {

        int[] nums = ArrayUtils.toArray("[1,5,11,5]", ",");
        System.out.println(new 分割等和子集_416().canPartition(nums));
    }

    public boolean canPartition(int[] nums) {
        int sum = Arrays.stream(nums).sum();
        if (sum % 2 != 0) return false;
        int target = sum / 2;
        int[][] dp = new int[nums.length][target + 1];
        // dp[0][j] 表示只有nums[0]的时候，和可能的值
        // dp[i][0] 表示target为0的时候的总和
//        for (int i = 0; i < nums.length; i++) {
//            dp[i][0] = 0; //默认就是0，可以不省略
//        }
        for (int j = target; j >= nums[0]; j--) {
            dp[0][j] = nums[0];
        }
        for (int i = 1; i < nums.length; i++) {
            for (int j = 0; j <= target; j++) {
                if (j >= nums[i]) { //可以装下nums[i]的时候，可以通过两种情况获取最大值； 1. 不装入物品i ； 2. 装入物品i， 那么物品 i-1时候的空间只有 j - nums[i]
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - nums[i]] + nums[i]);
                } else {
                    dp[i][j] = dp[i - 1][j]; // 容量j 小于第i个物品的重要装不了物品i
                }
            }
        }
        return dp[nums.length - 1][target] == target;
    }
}