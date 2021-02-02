package 二分法;

/**
 * 描述：剑指 Offer 53 - I. 在排序数组中查找数字 I
 * 查找数字出现的次数
 * 连接：https://leetcode-cn.com/problems/zai-pai-xu-shu-zu-zhong-cha-zhao-shu-zi-lcof/
 * 思路：通过二分法进行查找
 * <p>
 * Author：liuxing
 * Date：2021-02-02
 */
public class CountInSortedArray {

    public static void main(String[] args) {
        CountInSortedArray search = new CountInSortedArray();
        System.out.println(search.search(new int[]{5, 7, 7, 8, 8, 10}, 6));
    }


    public int search(int[] nums, int target) {
        return searchFirst(nums, target, 0, nums.length - 1);
    }

    private int searchFirst(int[] nums, int target, int start, int end) {
        if (start > end) {
            return 0;
        }
        int middle = (start + end) / 2;
        if (nums[middle] > target) {
            return searchFirst(nums, target, start, middle - 1);
        } else if (nums[middle] < target) {
            return searchFirst(nums, target, middle + 1, end);
        } else {
            return 1 + searchFirst(nums, target, start, middle - 1) + searchFirst(nums, target, middle + 1, end);
        }
    }
}
