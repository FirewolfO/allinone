package 集合;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class 两个数组的交集_349 {

    public static void main(String[] args) {
        int[] nums1 = new int[]{1, 2, 2, 1};
        int[] nums2 = new int[]{2, 2};
        int[] intersection = new 两个数组的交集_349().intersection(nums1, nums2);
        System.out.println(Arrays.toString(intersection));
    }

    public int[] intersection(int[] nums1, int[] nums2) {
        Set<Integer> set1 = new HashSet<>();
        for (int n : nums1) {
            set1.add(n);
        }
        Set<Integer> res = new HashSet<>();
        for (int n : nums2) {
            if (set1.contains(n)) {
                res.add(n);
            }
        }
        int[] resArray = new int[res.size()];
        int p = 0;
        Iterator<Integer> ite = res.iterator();
        while (ite.hasNext()) {
            resArray[p++] = ite.next();
        }
        return resArray;
    }

}
