package map;

import java.util.HashMap;
import java.util.Map;

public class 四数相加II_454 {

    public static void main(String[] args) {
        int[] nums1 = new int[]{1, 2};
        int[] nums2 = new int[]{-2, -1};
        int[] nums3 = new int[]{-1, 2};
        int[] nums4 = new int[]{0, 2};
        int sumCount = new 四数相加II_454().fourSumCount(nums1, nums2, nums3, nums4);
        System.out.println(sumCount);
    }

    public int fourSumCount(int[] nums1, int[] nums2, int[] nums3, int[] nums4) {
        Map<Integer, Integer> sumMap = new HashMap<>();
        for (int n1 : nums1) {// 计算出前两个数组的元素的和，并统计每个和的个数
            for (int n2 : nums2) {
                int sum = n1 + n2;
                int count = sumMap.getOrDefault(sum, 0);
                sumMap.put(sum, count + 1);
            }
        }
        int count = 0;
        for (int n3 : nums3) {
            for (int n4 : nums4) {
                int sum = n3 + n4;
                if (sumMap.containsKey(-sum)) { // 判断前两个数组中是否有和等于 0 - n3-n4 的数，以及个数
                    count += sumMap.get(-sum);
                }
            }
        }
        return count;
    }
}
