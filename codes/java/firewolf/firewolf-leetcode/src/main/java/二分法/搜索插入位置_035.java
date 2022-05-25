package 二分法;

/**
 * https://leetcode-cn.com/problems/search-insert-position/
 * 35. 搜索插入位置
 * 给定一个排序数组和一个目标值，在数组中找到目标值，并返回其索引。如果目标值不存在于数组中，返回它将会被按顺序插入的位置。
 * <p>
 * 请必须使用时间复杂度为 O(log n) 的算法。
 */
public class 搜索插入位置_035 {

    public static void main(String[] args) {
        int[] nums = new int[]{1, 3, 5, 6};
        int target = 2;
        System.out.println(new 搜索插入位置_035().searchInsert(nums, target));
    }

    public int searchInsert(int[] nums, int target) {
        return search(nums, 0, nums.length-1, target);
    }

    private int search(int[] nums, int start, int end, int target) {

        int mid = (start + end) / 2;
        if (nums[mid] == target) {
            return mid;
        }

        if(nums[start] > target){
            return start;
        }else if (nums[end] < target){
            return end+1;
        }

        if (target > nums[mid]) {
            return search(nums, mid + 1, end, target);
        } else {
            return search(nums, start, mid - 1, target);
        }
    }
}
