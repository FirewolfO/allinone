package 指针.双指针;

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
        int nums[] = new int[]{5, 7, 7, 8, 8, 10};
        int[] x = new 在排序数组中查找元素的第一个和最后一个位置_034().searchRange(nums, 8);
        System.out.println(Arrays.toString(x));
    }


    public int[] searchRange(int[] nums, int target) {
        int i = 0;
        int j = nums.length - 1;
        while (i <= j) {
            if (nums[i] == target && nums[j] == target) {
                return new int[]{i, j};
            }
            if (nums[i] < target) {
                i++;
            }
            if (nums[j] > target) {
                j--;
            }
        }

        return new int[]{-1, -1};
    }
}
