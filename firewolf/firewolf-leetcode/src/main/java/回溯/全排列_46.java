package 回溯;

import java.util.ArrayList;
import java.util.List;

class 全排列_46 {

    List<List<Integer>> res = new ArrayList<>();
    List<Integer> one = new ArrayList<>();

    public List<List<Integer>> permute(int[] nums) {
        permuteHelper(nums);
        return res;
    }

    private void permuteHelper(int[] nums) {
        if (one.size() == nums.length) { //当元素个数和数组长度一致的时候，收集
            res.add(new ArrayList<>(one));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (!one.contains(nums[i])) { //一个排列中不可以有重复元素
                one.add(nums[i]);
                permuteHelper(nums);
                one.remove(one.size() - 1);
            }
        }
    }
}