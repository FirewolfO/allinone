package easy;

/**
 * 704. 二分查找
 * 描述：https://leetcode-cn.com/problems/binary-search/
 * 思路：
 * 对于排好序的数组、通过二分查找完成
 * Author：liuxing
 * Date：2021-01-25
 */
public class BinarySearch {

    public static void main(String[] args) {
        int nums[] = {-1, 0, 3, 5, 9, 12};
        int target = 9;
        System.out.println(new BinarySearch().search(nums, target));
    }

    public int search(int[] nums, int target) {
        return search(target, 0, nums.length - 1, nums);
    }

    public int search(int target, int start, int end, int[] nums) {
        if (start > end) {
            return -1;
        }
        int middle = (start + end) / 2;
        if (nums[middle] == target) {
            return middle;
        }
        if (nums[middle] > target) {
            return search(target, start, middle - 1, nums);
        }
        return search(target, middle + 1, end, nums);
    }

}
