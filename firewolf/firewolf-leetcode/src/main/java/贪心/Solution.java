package 贪心;

import utils.ArrayUtils;

class Solution {

    public static void main(String[] args) {

        int[] ints = ArrayUtils.toArray("[1,3,7,5,10,3]", ",");
        System.out.println(new Solution().maxProfit(ints, 3));
    }


    public int maxProfit(int[] prices, int fee) {
        if (prices.length == 1) return -fee;
        int buy = prices[0];
        int maxProfit = 0;
        for (int i = 1; i < prices.length; i++) {
            int tmpMaxProfix = prices[i] - buy - fee;
            if (tmpMaxProfix < 0) { // 不再盈利了
                buy = prices[i];
                maxProfit += prices[i - 1] - buy - fee;
            }
        }
        return maxProfit;
    }
}