package 滑动窗口;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述：1. 两数之和
 * 连接：https://leetcode-cn.com/problems/two-sum/
 * Author：liuxing
 * Date：2021-02-02
 */
public class TwoNumSum {

    public static void main(String[] args) {
        int[] nums = {2, 7, 11, 15};
        int target = 9;
        System.out.println(new TwoNumSum().twoSum(nums, target));
    }

    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> allData = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int other = target - nums[i];
            if (allData.containsKey(other)) {
                return new int[]{i, allData.get(other)};
            }
            allData.put(nums[i], i);
        }
        return new int[0];
    }
}
