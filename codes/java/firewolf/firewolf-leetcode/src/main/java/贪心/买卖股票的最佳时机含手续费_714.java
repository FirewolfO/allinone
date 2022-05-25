package 贪心;

import utils.ArrayUtils;

class 买卖股票的最佳时机含手续费_714 {

    public static void main(String[] args) {
        int[] prices = ArrayUtils.toArray("[1,3,2,8,4,9]", ",");
        int fee = 2;
        int res = new 买卖股票的最佳时机含手续费_714().maxProfit(prices, fee);
        System.out.print(res);
    }

    public int maxProfit(int[] prices, int fee) {
        int maxProfit = 0;
        int minPrice = prices[0];
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] < minPrice) {
                minPrice = prices[i];
            } else if (prices[i] > minPrice + fee) {
                maxProfit += (prices[i] - minPrice - fee); // 收入
                minPrice = prices[i] - fee; // 可能持续持有，这样在明天收获利润的时候，才不会多减一次手续费！
            }
        }
        return maxProfit;
    }
}