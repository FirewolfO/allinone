package 双指针;

/**
 * 描述：4. 寻找两个正序数组的中位数
 * 连接：https://leetcode-cn.com/problems/median-of-two-sorted-arrays/
 * Author：liuxing
 * Date：2021-02-02
 */
public class MiddleNumInTowArray {


    public static void main(String[] args) {
        int[] nums = {1, 2};
        int[] nums2 = {3, 4};
        System.out.println(new MiddleNumInTowArray().findMedianSortedArrays(nums, nums2));
    }

    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int[] nums = merge(nums1, nums2);
        if (nums == null || nums.length == 0) {
            return 0d;
        }
        if (nums.length % 2 == 0) {
            return (nums[nums.length / 2] + nums[nums.length / 2 - 1]) / 2d;
        }
        return nums[nums.length / 2];
    }

    public int[] merge(int[] nums1, int[] nums2) {
        if (nums1 == null) {
            return nums2;
        }
        if (nums2 == null) {
            return nums1;
        }
        int i = 0;
        int j = 0;
        int nums[] = new int[nums1.length + nums2.length];
        int count = 0;
        while (i < nums1.length && j < nums2.length) {
            if (nums1[i] <= nums2[j]) {
                nums[count++] = nums1[i++];
            } else if (nums2[j] <= nums1[i]) {
                nums[count++] = nums2[j++];
            }
        }
        if (i < nums1.length) {
            while (i < nums1.length) {
                nums[count++] = nums1[i++];
            }
        }
        if (j < nums2.length) {
            while (j < nums2.length) {
                nums[count++] = nums2[j++];
            }
        }
        return nums;
    }
}
