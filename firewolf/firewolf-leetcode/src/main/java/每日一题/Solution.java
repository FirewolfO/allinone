package 每日一题;

import utils.ArrayUtils;

class Solution {
    public static void main(String[] args) {
        int[] ints = ArrayUtils.toArray("[0,1,0,2,1,0,1,3,2,1,2,1]", ",");
        System.out.println(new Solution().trap(ints));
    }

    public int trap(int[] height) {
        int leftH = 0;
        int leftIndex = -1;

        int sum = 0;
        for (int i = 1; i < height.length; i++) {
            if (height[i] > height[i - 1]) {
                if (leftIndex != -1) {
                    sum += (i - leftIndex - 1) * Math.min(leftH, height[i]);
                    System.out.println("i=" + i + " , sum=" + sum);
                }
                leftH = height[i];
                leftIndex = i;
            }
        }
        return sum;
    }
}