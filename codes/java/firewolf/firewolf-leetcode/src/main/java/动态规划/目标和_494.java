package 动态规划;

import utils.ArrayUtils;

import java.util.Arrays;

public class 目标和_494 {


    public static void main(String[] args) {
        int[] ints = ArrayUtils.toArray("[1,1,1,1,1]", ",");
        int targetSumWays = new 目标和_494().findTargetSumWays(ints, 3);
        System.out.println(targetSumWays);
    }

    public int findTargetSumWays(int[] nums, int target) {
        int sum = Arrays.stream(nums).sum();
        // 如果target超过了总和，是不可能得到target的
        if (Math.abs(target) > sum) return 0;

        //可能得和为： [-sum,sum]，加上0，一共有2*sum+1个
        // dp[i][j] 表示 从nums[0] ~ nums[i] 和为 j 的可能个数， 那么这个值可能通过 nums[0] ~ nums[i-1] 的和加上或者减去nums[i-1]，
        // j的取值为 [-sum,sum]，为了数组遍历，用0表示-sum,1表示1-sum,  依次类推，j表示的和是 j-sum

        // 所以，转换方程为：dp[i][j] = dp[i-1][j-nums[i]]+dp[i-1][j+nums[i]]
        int[][] dp = new int[nums.length][2 * sum + 1];

        // 根据转换方程，我们只需要对dp[0][j]进行初始化，只有当 j 表示的的绝对值是nums[0]的时候，才有一种可能，其他都不可能，
        // 即：j-sum = nums[0]  或者 j-sum = -nums[0]， 因此 j 等于 sum+nums[0]或者sum-nums[0]
        // 因此，初始化如下：
        dp[0][sum + nums[0]] = 1;
        dp[0][sum - nums[0]] = 1;
        // 然而，如果nums[0] 等于0的话，+0 和-0都能得到和为0，因此此时， 和为0的可能为为2；
        if (nums[0] == 0) {
            dp[0][sum] = 2;
        }

        //遍历
        for (int i = 1; i < nums.length; i++) {
            for (int j = 0; j < sum * 2 + 1; j++) {
                if (j - nums[i] < 0) { //低于最小和，只能通过+获取
                    dp[i][j] = dp[i - 1][j + nums[i]];
                } else if (j + nums[i] > 2 * sum) { // 超过了最大数，只能通过减获取
                    dp[i][j] = dp[i - 1][j - nums[i]];
                } else {
                    dp[i][j] = dp[i - 1][j - nums[i]] + dp[i - 1][j + nums[i]];
                }
            }
        }
        //和是target，即：j-sum = target， 所以 j = target+sum
        return dp[nums.length - 1][target + sum];
    }
}
