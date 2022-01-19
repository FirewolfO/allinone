package 回溯;

import java.util.ArrayList;
import java.util.List;

class 组合总和_39 {

    public static void main(String[] args) {
        int[] candidates = new int[]{2, 3, 6, 7};
        int target = 7;
        List<List<Integer>> lists = new 组合总和_39().combinationSum(candidates, target);
        System.out.println(lists);
    }

    List<List<Integer>> res = new ArrayList<>();
    List<Integer> one = new ArrayList<>();

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        combinationSumHelper(0, candidates, target);
        return res;
    }

    private void combinationSumHelper(int start, int[] candidates, int target) {
        if (target == 0) {
            res.add(new ArrayList<>(one));
            return;
        }
        for (int i = start; i < candidates.length; i++) {
            if (target >= candidates[i]) {
                one.add(candidates[i]);
                combinationSumHelper(i, candidates, target - candidates[i]);
                one.remove(one.size() - 1);
            }
        }
    }
}