package 贪心;

class 最大子数组和_53 {

    public static void main(String[] args) {
        int[] nums = new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4};
        new 最大子数组和_53().maxSubArray(nums);
    }

    public int maxSubArray(int[] nums) {
        int maxSum = Integer.MIN_VALUE;
        int tmpSum = 0;
        for (int i = 0; i < nums.length; i++) {
            tmpSum += nums[i];
            if (tmpSum > maxSum) maxSum = tmpSum; // 如果比和要大，就更新
            if (tmpSum < 0) tmpSum = 0; // 如果小于0，对整体是拖累，丢弃
        }
        return maxSum;
    }
}