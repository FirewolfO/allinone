package 贪心;

import java.util.Arrays;

class 分发糖果_135 {

    public static void main(String[] args) {
        int[] ratings = new int[]{1, 2, 3, 4};
        System.out.println(new 分发糖果_135().candy(ratings));
    }

    public int candy(int[] ratings) {
        int[] candies = new int[ratings.length];// 每个小朋友的糖果数
        for (int i = 0; i < ratings.length; i++) {
            if (i > 0 && ratings[i] > ratings[i - 1]) {
                candies[i] = candies[i - 1] + 1;
            } else {
                candies[i] = 1;
            }
        }
        int sum = 0;
        int iCandy = 0;
        for (int i = ratings.length - 1; i >= 0; i--) {
            if (i < ratings.length - 1 && ratings[i] > ratings[i + 1]) {
                iCandy = candies[i + 1] + 1;
            } else {
                iCandy = 1;
            }
            iCandy = Math.max(candies[i], iCandy);
            candies[i] = iCandy; // 只是求和并不需要重新赋值，这里是为了记录最终的发放量，
            sum += iCandy;
        }
        System.out.println(Arrays.toString(candies));
        return sum;
    }
}