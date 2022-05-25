package 动态规划;

import utils.ArrayUtils;

class 使用最小花费爬楼梯_746 {
    public static void main(String[] args) {
        int[] cost = ArrayUtils.toArray("[10,15,20]", ",");
        System.out.println(new 使用最小花费爬楼梯_746().minCostClimbingStairs(cost));
    }

    /**********************常规动态规划***********************/
    public int minCostClimbingStairs(int[] cost) {
        int[] minCost = new int[cost.length + 1];
        minCost[0] = minCost[1] = 0;
        for (int i = 2; i < minCost.length; i++) {
            minCost[i] = Math.min(minCost[i - 2] + cost[i - 2], minCost[i - 1] + cost[i - 1]);
        }
        return minCost[minCost.length - 1];
    }

    /**************************动态规划，O(1)，滚动数组****************************/
    public int minCostClimbingStairs2(int[] cost) {
        int minCost_2 = 0;
        int minCost_1 = 0;
        int tmp;
        for (int i = 2; i < cost.length + 1; i++) {
            tmp = minCost_1;
            minCost_1 = Math.min(minCost_2 + cost[i - 2], minCost_1 + cost[i - 1]);
            minCost_2 = tmp;
        }
        return minCost_1;
    }
}