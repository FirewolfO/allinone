package 动态规划;

import utils.ArrayUtils;

import java.util.Arrays;

class 最后一块石头的重量II_1049 {

    public static void main(String[] args) {
        int[] stones = ArrayUtils.toArray("[2,7,4,1,8,1]", ",");
        System.out.println(new 最后一块石头的重量II_1049().lastStoneWeightII(stones));
    }

    public int lastStoneWeightII(int[] stones) {
        //总体思路：把所有的石头划分成接近相等的两堆，然后，用大的-小的，这样，问题就和 [416. 分割等和子集](https://leetcode-cn.com/problems/partition-equal-subset-sum) 等价了

        int sum = Arrays.stream(stones).sum();
        int target = sum / 2;
        int[][] dp = new int[stones.length][target + 1];
        // dp[0][j] 表示只有stones[0]的时候，和可能的值
        // dp[i][0] 表示target为0的时候的总和
//        for (int i = 0; i < stones.length; i++) {
//            dp[i][0] = 0; //默认就是0，可以不省略
//        }
        for (int j = target; j >= stones[0]; j--) {
            dp[0][j] = stones[0];
        }
        for (int i = 1; i < stones.length; i++) {
            for (int j = 0; j <= target; j++) {
                if (j >= stones[i]) { //可以装下stones[i]的时候，可以通过两种情况获取最大值； 1. 不装入物品i ； 2. 装入物品i， 那么物品 i-1时候的空间只有 j - stones[i]
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - stones[i]] + stones[i]);
                } else {
                    dp[i][j] = dp[i - 1][j]; // 容量j 小于第i个物品的重要装不了物品i
                }
            }
        }
        return sum - 2 * dp[stones.length - 1][target];
    }
}