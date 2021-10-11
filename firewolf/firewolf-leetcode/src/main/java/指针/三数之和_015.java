package 指针;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 15. 三数之和
 * 给你一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，使得 a + b + c = 0 ？请你找出所有和为 0 且不重复的三元组。
 * <p>
 * 注意：答案中不可以包含重复的三元组。
 */
public class 三数之和_015 {

    public static void main(String[] args) {
        List<List<Integer>> lists = new 三数之和_015().threeSum(new int[]{-4, -2, 1, -5, -4, -4, 4, -2, 0, 4, 0, -2, 3, 1, -5, 0});
        System.out.println(lists);
    }

    public List<List<Integer>> threeSum(int[] nums) {
        if (nums.length < 3) {
            return new ArrayList<>();
        }
        Arrays.sort(nums);
        return threeSumOfSortedArray(nums, 0);
    }

    /**
     * 三个数字的和
     *
     * @param nums 数组字符串
     * @param sum  总和
     * @return
     */
    public List<List<Integer>> threeSumOfSortedArray(int[] nums, int sum) {
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i <= nums.length - 3; i++) {
//            if (nums[i] > sum) { // 第一个元素已经大于总和，丢弃，直接退出
//                return result;
//            }
            // 去掉重复元素
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int target = sum - nums[i];
            int k = nums.length - 1;
            for (int j = i + 1; j <= nums.length - 2; j++) {
//                if (nums[j] > target) { // nums[j] 已经大于总和，不再求和了
//                    return result;
//                }
                if (j > i + 1 && nums[j] == nums[j - 1]) { // 去重
                    continue;
                }
                while (k > j && nums[j] + nums[k] > target) { // 由于nums[j]不停增大，在满足nums[j] + nums[k] > target条件下，k可以一直减小
                    k--;
                }
                if (k == j) { // 重合了，进行下一个j
                    continue;
                }
//                while (k > j + 1 && nums[k] == nums[k - 1]) { // 去重，其实这个已经没有必要，因为，前两个元素不可能一样，那么这个肯定也不会一样了
//                    k--;
//                }
                if (nums[j] + nums[k] == target) { // 找到了
                    List<Integer> one = new ArrayList<>();
                    one.add(nums[i]);
                    one.add(nums[j]);
                    one.add(nums[k]);
                    result.add(one);
                }
            }
        }
        return result;
    }

}
