package 回溯;

import java.util.ArrayList;
import java.util.List;

import static utils.TreeUtils.*;

class 路径总和_112 {

    public static void main(String[] args) {
        TreeNode<Integer> root = buildBFSTree(5, 4, 8, 11, null, 13, 4, 7, 2, null, null, null, 1);
        System.out.println(new 路径总和_112().hasPathSum(root, 22));
        System.out.println(new 路径总和_112().hasPathSumRecursion(root, 22));
    }

    // ------------- 回溯----------------
    public boolean hasPathSum(TreeNode<Integer> root, int targetSum) {
        if (root == null) {
            return false;
        }
        return hasPathSum(root, new ArrayList<>(), targetSum);
    }

    private boolean hasPathSum(TreeNode<Integer> root, List<Integer> vals, int targetSum) {
        vals.add(root.val);
        if (root.left == null && root.right == null) {
            int sum = 0;
            for (int val : vals) sum += val;
            if (sum == targetSum) return true;
        }
        if (root.left != null) {
            if (hasPathSum(root.left, vals, targetSum)) return true;
            vals.remove(vals.size() - 1); // 回溯
        }
        if (root.right != null) {
            if (hasPathSum(root.right, vals, targetSum)) return true;
            vals.remove(vals.size() - 1); // 回溯
        }
        return false;
    }

    //--------- 直接递归解法-------------
    public boolean hasPathSumRecursion(TreeNode<Integer> root, int targetSum) {
        if (root == null) {
            return false;
        }
        if (root.left == null && root.right == null) {
            return root.val == targetSum;
        }
        return hasPathSumRecursion(root.left, targetSum - root.val) || hasPathSumRecursion(root.right, targetSum - root.val);
    }
}