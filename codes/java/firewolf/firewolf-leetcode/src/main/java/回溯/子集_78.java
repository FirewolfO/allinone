package 回溯;

import java.util.ArrayList;
import java.util.List;

class 子集_78 {

    public static void main(String[] args) {
        int[] nums = new int[]{1, 2, 3};
        System.out.println(new 子集_78().subsets(nums));
    }

    List<List<Integer>> res = new ArrayList<>();
    List<Integer> one = new ArrayList<>();

    public List<List<Integer>> subsets(int[] nums) {
        List<Integer> empty = new ArrayList<>();
        res.add(empty);
        subsetsHelper(nums, 0);
        return res;
    }

    private void subsetsHelper(int[] nums, int start) {
        res.add(new ArrayList<>(one));
        if (start > nums.length) {
            return;
        }
        for (int i = start; i < nums.length; i++) { //每个节点都要进行收集
            one.add(nums[i]);
            subsetsHelper(nums, i + 1);
            one.remove(one.size() - 1);
        }
    }
}