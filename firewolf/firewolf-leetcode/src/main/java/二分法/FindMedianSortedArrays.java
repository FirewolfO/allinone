package 二分法;

class FindMedianSortedArrays {

    public static void main(String[] args) {
        int[] nums1 = {0, 0, 0, 0, 0};
        int[] nums2 = {-1, 0, 0, 0, 0, 0, 1};
        System.out.println(new FindMedianSortedArrays().findMedianSortedArrays(nums1, nums2));
    }


    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if (nums1.length == 0 && nums2.length == 0) {
            return 0;
        }
        int left = 0, right = 0;
        int aLen = nums1.length;
        int bLen = nums2.length;
        int m = 0, n = 0, i = 0;
        int middle = (aLen + bLen) / 2 + 1;
        while (i < middle) {
            left = right;
            i++;
            if (m < aLen && (n >= bLen || nums1[m] < nums2[n])) {
                right = nums1[m++];
            } else {
                right = nums2[n++];
            }
        }
        if ((aLen + bLen) % 2 == 1) { //奇数个
            return right;
        } else {
            return (left + right) / 2.0;
        }
    }
}