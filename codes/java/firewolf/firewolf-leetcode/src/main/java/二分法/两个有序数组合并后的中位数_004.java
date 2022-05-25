package 二分法;

/**
 * 4. 寻找两个正序数组的中位数
 * 给定两个大小分别为 m 和 n 的正序（从小到大）数组 nums1 和 nums2。请你找出并返回这两个正序数组的 中位数 。
 */
public class 两个有序数组合并后的中位数_004 {

    public static void main(String[] args) {
        double mid = new 两个有序数组合并后的中位数_004().findMedianSortedArrays(new int[]{1, 2}, new int[]{3, 4});
        System.out.println(mid);
    }

    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) {
            return findMedianSortedArrays(nums2, nums1);
        }
        int m = nums1.length;
        int n = nums2.length;
        int left = 0;
        int right = m;
        int midLeft = 0;
        int midRight = 0;
        while ((left <= right)) {
            int i = (left + right) / 2;
            int j = (m + n + 1) / 2 - i;

            int nums_i_1 = i == 0 ? Integer.MIN_VALUE : nums1[i - 1];
            int nums_i = i == m ? Integer.MAX_VALUE : nums1[i];
            int nums_j_1 = j == 0 ? Integer.MIN_VALUE : nums2[j - 1];
            int nums_j = j == n ? Integer.MAX_VALUE : nums2[j];
            if (nums_i_1 <= nums_j) {
                midLeft = Math.max(nums_i_1, nums_j_1);
                midRight = Math.min(nums_i, nums_j);
                left = i + 1;
            } else {
                right = i - 1;
            }
        }
        return (m + n) % 2 == 1 ? midLeft : (double) (midLeft + midRight) / 2.0;
    }
}
