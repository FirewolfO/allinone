package 集合;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class 两个数组的交集II {

    public static void main(String[] args) {
        int[] nums1 = new int[]{4, 9, 5};
        int[] nums2 = new int[]{9, 4, 9, 8, 4};
        int[] intersect = new 两个数组的交集II().intersect(nums1, nums2);
        System.out.println(Arrays.toString(intersect));
    }

    public int[] intersect(int[] nums1, int[] nums2) {
        Map<Integer, Integer> map1 = new HashMap<>();
        for (int i = 0; i < nums1.length; i++) {
            int count = map1.getOrDefault(nums1[i], 0);
            map1.put(nums1[i], count + 1);
        }
        int[] res = new int[nums1.length];
        int i = 0;
        int resIndex = 0;
        for (; i < nums2.length; i++) {
            int n = nums2[i];
            if (map1.containsKey(n)) {
                res[resIndex++] = n;
                int count = map1.get(n);
                count--;
                if (count == 0) {
                    map1.remove(n);
                } else {
                    map1.put(n, count);
                }
            }
        }
        return Arrays.copyOfRange(res, 0, resIndex);
    }
}