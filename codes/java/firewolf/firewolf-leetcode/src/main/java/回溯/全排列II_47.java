package 回溯;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/***
 * 某层已经用过的不能重复
 * 一个排列中，已经用了的元素也不能使用
 */
class 全排列II_47 {

    public static void main(String[] args) {
        int[] nums = new int[]{1, 1, 2};
        System.out.println(new 全排列II_47().permuteUnique(nums));
    }

    private List<List<Integer>> res = new ArrayList<>();
    private List<Integer> one = new ArrayList<>();
    int[] oneUsed;

    public List<List<Integer>> permuteUnique(int[] nums) {
        oneUsed = new int[nums.length];
        Arrays.sort(nums);
        permuteUniqueHelper(nums);
        return res;
    }

    private void permuteUniqueHelper(int[] nums) {
        if (one.size() == nums.length) {
            res.add(new ArrayList<>(one));
            return;
        }
        int[] levelUsed = new int[21]; //标记某一层被使用了的
        for (int i = 0; i < nums.length; i++) {
            if (oneUsed[i] == 1 || levelUsed[nums[i] + 10] == 1) continue;
            one.add(nums[i]);
            oneUsed[i] = 1;
            levelUsed[nums[i] + 10] = 1;
            permuteUniqueHelper(nums);
            one.remove(one.size() - 1); //回溯元素
            oneUsed[i] = 0;
        }
    }
}