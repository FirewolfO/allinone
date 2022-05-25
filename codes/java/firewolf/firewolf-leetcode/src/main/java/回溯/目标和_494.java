package 回溯;

import utils.ArrayUtils;

class 目标和_494 {

    public static void main(String[] args) {
        int[] nums = ArrayUtils.toArray("[1,1,1,1,1]", ",");
        int targetSumWays = new 目标和_494().findTargetSumWays(nums, 3);
        System.out.println(targetSumWays);
    }

    int count = 0;

    public int findTargetSumWays(int[] nums, int target) {
        backtracking(nums, 0, target);
        return count;
    }

    private void backtracking(int[] nums, int index, int target) {
        if (index == nums.length) {
            if (target == 0) {
                count++;
            }
            return;
        }
        backtracking(nums, index + 1, target + nums[index]);
        backtracking(nums, index + 1, target - nums[index]);
    }
}