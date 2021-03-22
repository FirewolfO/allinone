package 双指针;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Description: 15. 三数之和
 * https://leetcode-cn.com/problems/3sum/
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2021/3/22 5:44 下午
 */
public class ThirdNumAdd {

    public static void main(String[] args) {
        int[] nums = {-1, 0, 1, 2, -1, -4};
        System.out.println(new ThirdNumAdd().threeSum(nums));
    }

    public List<List<Integer>> threeSum(int[] nums) {
        if (nums.length < 3) {
            return new ArrayList<>();
        }
        Arrays.sort(nums);
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int target = 0 - nums[i];
            int thrid = nums.length - 1;
            for (int j = i + 1; j < thrid; j++) {
                if (j > i + 1 && nums[j] == nums[j - 1]) {
                    continue;
                }
                int m = thrid;
                while (m > j + 1 && nums[m] + nums[j] > target) {
                    m--;
                }
                if (nums[m] + nums[j] == target) {
                    List<Integer> l = new ArrayList<>();
                    l.add(nums[i]);
                    l.add(nums[j]);
                    l.add(nums[m]);
                    res.add(l);
                    thrid = m;
                }
            }
        }
        return res;
    }
}
