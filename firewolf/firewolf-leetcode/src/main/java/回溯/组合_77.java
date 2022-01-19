package 回溯;

import java.util.ArrayList;
import java.util.List;

class 组合_77 {

    public static void main(String[] args) {
        List<List<Integer>> combine = new 组合_77().combine(4, 2);
        System.out.println(combine);
    }

    List<Integer> one = new ArrayList<>();
    List<List<Integer>> res = new ArrayList<>();

    public List<List<Integer>> combine(int n, int k) {
        combine(1, n, k);
        return res;
    }

    private void combine(int start, int n, int k) {
        if (one.size() == k) {
            res.add(new ArrayList(one));
            return;
        }
        for (int i = start; i <= one.size() + n + 1 - k; i++) { //one.size() + (n-i)+1 >=k //减枝
            one.add(i);
            combine(i + 1, n, k);
            one.remove(one.size() - 1);
        }
    }
}