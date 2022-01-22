package 回溯;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class 子集II_90 {

    public static void main(String[] args) {
        int[] nums = new int[]{1, 2, 2};
        List<List<Integer>> lists = new 子集II_90().subsetsWithDup(nums);
        System.out.println(lists);
    }

    List<List<Integer>> res = new ArrayList<>();
    List<Integer> one = new ArrayList<>();

    public List<List<Integer>> subsetsWithDup(int[] nums) {
        List<Integer> empty = new ArrayList<>();
        res.add(empty);
        Arrays.sort(nums);
        subsetsHelper(nums, 0);
        return res;
    }

    private void subsetsHelper(int[] nums, int start) {
        res.add(new ArrayList<>(one));
        if (start > nums.length) {
            return;
        }
        for (int i = start; i < nums.length; i++) { //每个节点都要进行收集
            if (i > start && nums[i] == nums[i - 1]) continue;
            one.add(nums[i]);
            subsetsHelper(nums, i + 1);
            one.remove(one.size() - 1);
        }
    }
}