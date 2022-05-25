package 贪心;

import java.util.stream.IntStream;

class K次取反后最大化的数组和_1005 {

    public static void main(String[] args) {
        int[] nums = new int[]{2, -3, -1, 5, -4};
        int k = 2;
        int sum = new K次取反后最大化的数组和_1005().largestSumAfterKNegations(nums, 2);
        System.out.println(sum);
    }

    public int largestSumAfterKNegations(int[] nums, int k) {
        // 按照绝对值排序
        nums = IntStream.of(nums).boxed().sorted((o1, o2) -> Math.abs(o2) - Math.abs(o1)).mapToInt(Integer::intValue).toArray();
        for (int i = 0; i < nums.length; i++) {
            if (k > 0) {
                if (nums[i] < 0) { // 负数取反
                    nums[i] = -nums[i];
                    k--;
                }
            } else { // 已经反转了K个了，结束
                break;
            }
        }
        if (k > 0) {
            if (k % 2 == 1) { // 如果还剩的次数是奇数次，需要做处理
                nums[nums.length - 1] = -nums[nums.length - 1]; // 对最小的绝对值取反，
            }
        }
        return IntStream.of(nums).sum();
    }
}