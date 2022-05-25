package 指针.双指针;

import java.util.Arrays;

/**
 * 16. 最接近的三数之和
 * 给定一个包括 n 个整数的数组 nums 和 一个目标值 target。找出 nums 中的三个整数，使得它们的和与 target 最接近。返回这三个数的和。假定每组输入只存在唯一答案。
 */
public class 最接近的三数之和_016 {

    public static void main(String[] args) {
        int[] nums = new int[]{1, 1, 1, 0};
        int i = new 最接近的三数之和_016().threeSumClosest(nums, 100);
        System.out.println(i);
    }

    // [-4,-1, 1, 2]
    public int threeSumClosest(int[] nums, int target) {
        int res = 10000;
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 2; i++) {
            int left = i + 1;
            int right = nums.length - 1;
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                if (sum == target) {
                    return target;
                }
                if (Math.abs(sum - target) < Math.abs(res - target)) { // 更近了，更新
                    res = sum;
                }
                if (sum > target) {
                    right--;
                } else {
                    left++;
                }
            }
        }
        return res;
    }

}
