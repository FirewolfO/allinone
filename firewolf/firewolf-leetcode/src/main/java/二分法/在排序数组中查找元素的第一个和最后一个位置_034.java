package 二分法;

import java.util.Arrays;

/**
 * https://leetcode-cn.com/problems/find-first-and-last-position-of-element-in-sorted-array/
 * 给定一个按照升序排列的整数数组 nums，和一个目标值 target。找出给定目标值在数组中的开始位置和结束位置。
 * <p>
 * 如果数组中不存在目标值 target，返回 [-1, -1]。
 * <p>
 * 进阶：
 * <p>
 * 你可以设计并实现时间复杂度为 O(log n) 的算法解决此问题吗？
 */
public class 在排序数组中查找元素的第一个和最后一个位置_034 {

    public static void main(String[] args) {
        int nums[] = new int[]{1};
        int[] x = new 在排序数组中查找元素的第一个和最后一个位置_034().searchRange(nums, 1);
        System.out.println(Arrays.toString(x));
    }


    public int[] searchRange(int[] nums, int target) {
        if (nums.length == 0) { // 没有元素的时候
            return new int[]{-1, -1};
        }

        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int mid = (left + right) >>> 1;
            if (nums[mid] >= target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        if (nums[right] != target) {
            return new int[]{-1, -1};
        }

        int L = right;
        left = 0;
        right = nums.length - 1;
        int R = L;
        while (left < right) {
            int mid = (left + right) >>> 1;
            if (nums[mid] > target) {
                right = mid;
            } else {
                R = mid;
                left = mid + 1;
            }
        }
        return new int[]{L, R};
    }
}
