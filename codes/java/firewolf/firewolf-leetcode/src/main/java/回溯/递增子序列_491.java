package 回溯;

import java.util.ArrayList;
import java.util.List;

class 递增子序列_491 {

    public static void main(String[] args) {
        int[] nums = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 1, 1, 1, 1};
        System.out.println(new 递增子序列_491().findSubsequences(nums));
    }

    List<List<Integer>> res = new ArrayList<>();
    List<Integer> one = new ArrayList<>();

    public List<List<Integer>> findSubsequences(int[] nums) {
        findSubsequencesHelper(nums, 0);
        return res;
    }

    private void findSubsequencesHelper(int[] nums, int start) {
        if (one.size() > 1) {
            res.add(new ArrayList<>(one)); // 这里不要返回，继续
        }
//        List<Integer> used = new ArrayList<>();//记录本层已经使用过的元素

        int[] used = new int[201]; //用数组记录本层已经使用过的元素，元素从 -100到100，共201个元素
        for (int i = start; i < nums.length; i++) {
            if ((one.size() > 0 && nums[i] < one.get(one.size() - 1)) ||
//                    used.contains(nums[i]))
                    used[nums[i] + 100] == 1)
                continue;
//            used.add(nums[i]);
            used[nums[i] + 100] = 1;
            one.add(nums[i]);
            findSubsequencesHelper(nums, i + 1);
            one.remove(one.size() - 1);
        }
    }
}