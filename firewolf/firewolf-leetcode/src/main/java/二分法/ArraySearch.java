package 二分法;

/**
 * 描述：33. 搜索旋转排序数组
 * 连接：https://leetcode-cn.com/problems/search-in-rotated-sorted-array/
 * Author：liuxing
 * Date：2021-02-02
 */
public class ArraySearch {

    public static void main(String[] args) {
        int nums[] = {4, 5, 6, 7, 0, 1, 2};
        int target = 0;
        System.out.println(new ArraySearch().search(nums, target));
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
        int aa = search(target, start, middle - 1, nums);
        if (aa != -1) {
            return aa;
        }
        return search(target, middle + 1, end, nums);
    }

}
