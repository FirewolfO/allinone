package 回溯;

import java.util.LinkedList;

import static utils.TreeUtils.TreeNode;
import static utils.TreeUtils.buildBFSTree;

class 路径总和_112 {

    public static void main(String[] args) {
        TreeNode<Integer> root = buildBFSTree(5, 4, 8, 11, null, 13, 4, 7, 2, null, null, null, 1);
        System.out.println(new 路径总和_112().hasPathSum(root, 22));
        System.out.println(new 路径总和_112().hasPathSumRecursion(root, 22));
    }

    // ------------- 回溯----------------
    public boolean hasPathSum(TreeNode<Integer> root, int targetSum) {
        LinkedList<Integer> vals = new LinkedList<>();
        return hasPathSum(root, vals, targetSum);
    }

    private boolean hasPathSum(TreeNode<Integer> root, LinkedList<Integer> vals, int targetSum) {
        if (root == null) return false;
        vals.addLast(root.val);
        targetSum -= root.val;
        if (root.left == null && root.right == null && targetSum == 0) return true;
        boolean res = hasPathSum(root.left, vals, targetSum) || hasPathSum(root.right, vals, targetSum);
        vals.removeLast();
        return res;
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