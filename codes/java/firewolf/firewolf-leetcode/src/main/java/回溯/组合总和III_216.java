package 回溯;

import java.util.ArrayList;
import java.util.List;

public class 组合总和III_216 {

    public static void main(String[] args) {
        List<List<Integer>> lists = new 组合总和III_216().combinationSum3(3, 7);
        System.out.println(lists);
    }

    List<List<Integer>> res = new ArrayList<>();
    List<Integer> one = new ArrayList<>();

    public List<List<Integer>> combinationSum3(int k, int n) {
        combinationSum3Helper(1, k, n);
        return res;
    }

    private void combinationSum3Helper(int start, int k, int n) {
        if (one.size() == k) {
            if (n == 0) { // 剩余和为0，并且元素已经够了n个了，开始收集
                res.add(new ArrayList<>(one));
            }
            return;
        }
        for (int i = start; i <= 10 + one.size() - k; i++) { //one.size()+(9-i)+1 >=k
            if (n >= i) { //再次剪枝，如果总和比i还小，那就没必须要再继续了
                one.add(i);
                combinationSum3Helper(i + 1, k, n - i);
                one.remove(one.size() - 1);
            }
        }
    }
}
