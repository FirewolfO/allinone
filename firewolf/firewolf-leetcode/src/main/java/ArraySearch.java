/**
 * 33. 搜索旋转排序数组
 * 描述：https://leetcode-cn.com/problems/search-in-rotated-sorted-array/
 * Author：liuxing
 * Date：2021-01-25
 */
public class ArraySearch {


    public static void main(String[] args) {
        int nums[] = {1};
        int target = 0;
        System.out.println(search(nums, target));
    }

    public static int search(int[] nums, int target) {
        return search(target, 0, nums.length - 1, nums);
    }

    public static int search(int target, int start, int end, int[] nums) {
        if (start > end) {
            return -1;
        }
        int middle = (start + end) / 2;
        if (nums[middle] == target) {
            return middle;
        }
        int index = search(target, middle + 1, end, nums);
        if (index != -1) {
            return index;
        } else {
            return search(target, start, middle - 1, nums);
        }
    }

}
