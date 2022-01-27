package 贪心;

class Solution {

    public static void main(String[] args) {
        int[] nums = new int[]{3, 2, 1, 0, 4};
        System.out.println(new Solution().canJump(nums));
    }

    public boolean canJump(int[] nums) {
        // if(nums.length == 1) return true; // 就一个位置，直接就在这个位置，可以达到；
        int maxCover = 0;
        for (int i = 0; i <= maxCover; i++) { //在当前能够覆盖的范围，看能否到达最后的位置
            maxCover = Math.max(maxCover, i + nums[i]); //获取当前位置能够覆盖到的最大位置
            if (maxCover >= nums.length - 1) return true; // 能覆盖到最后一个位置，证明可以达到
        }
        return false;
    }
}