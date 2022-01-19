package 回溯;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/***
 * 40. 组合总和 II
 * 给你一个由候选元素组成的集合 candidates 和一个目标数 target ，找出 candidates 中所有可以使数字和为 target 的组合。
 *
 * candidates 中的每个元素在每个组合中只能使用 一次 。
 *
 * 注意：解集不能包含重复的组合。
 */
class 组合总和II_40 {

    public static void main(String[] args) {
        int[] canditates = new int[]{10, 1, 2, 7, 6, 1, 5};
        int target = 8;
        List<List<Integer>> lists = new 组合总和II_40().combinationSum2(canditates, target);
        System.out.println(lists);
    }

    List<List<Integer>> res = new ArrayList<>();
    List<Integer> one = new ArrayList<>();

    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        Arrays.sort(candidates);
        combinationSum2Helper(0, candidates, target);
        return res;
    }

    private void combinationSum2Helper(int start, int[] candidates, int target) {
        if (target == 0) {
            res.add(new ArrayList<>(one));
            return;
        }
        for (int i = start; i < candidates.length; i++) {
            if (i > start && candidates[i] == candidates[i - 1]) continue; //跳过重复数据
            if (target >= candidates[i]) { //剪枝
                one.add(candidates[i]);
                combinationSum2Helper(i + 1, candidates, target - candidates[i]);
                one.remove(one.size() - 1);//回溯
            }
        }
    }
}