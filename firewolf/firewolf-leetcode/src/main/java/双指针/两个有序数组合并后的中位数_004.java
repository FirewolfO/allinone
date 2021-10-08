package 双指针;

/**
 * 4. 寻找两个正序数组的中位数
 * 给定两个大小分别为 m 和 n 的正序（从小到大）数组 nums1 和 nums2。请你找出并返回这两个正序数组的 中位数 。
 */
class 两个有序数组合并后的中位数_004 {

    public static void main(String[] args) {
        double x = new 两个有序数组合并后的中位数_004().findMedianSortedArrays(new int[]{1, 2}, new int[]{3, 4});
        System.out.println(x);
    }

    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if (nums1.length == 0 && nums2.length == 0) {
            return 0;
        }
        int totalLen = nums1.length + nums2.length;
        int rightIndex = totalLen / 2;
        int leftIndex = (totalLen - 1) / 2;

        if (nums1.length == 0) {
            return (nums2[leftIndex] + nums2[rightIndex]) / 2.0;
        }
        if (nums2.length == 0) {
            return (nums1[leftIndex] + nums1[rightIndex]) / 2.0;
        }
        int key = 0;
        int left = 0;
        int right = 0;
        int min = 0;
        int max = 0;
        while (key <= rightIndex) {
            if (left < nums1.length && right < nums2.length) {
                if (nums1[left] <= nums2[right]) {
                    min = max;
                    max = nums1[left++];
                } else {
                    min = max;
                    max = nums2[right++];
                }
            } else if (left < nums1.length) {
                min = max;
                max = nums1[left++];
            } else if (right < nums2.length) {
                min = max;
                max = nums2[right++];
            }
            key++;
        }
        return totalLen % 2 == 0 ? (min + max) / 2.0 : max;
    }
}