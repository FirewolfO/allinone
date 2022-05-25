package 回溯;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static utils.TreeUtils.TreeNode;
import static utils.TreeUtils.buildBFSTree;

class 路径总和II_113 {

    public static void main(String[] args) {
        TreeNode<Integer> root = buildBFSTree(5, 4, 8, 11, null, 13, 4, 7, 2, null, null, 5, 1);
        System.out.println(new 路径总和II_113().pathSum(root, 22));
    }

    public List<List<Integer>> pathSum(TreeNode<Integer> root, int targetSum) {
        List<List<Integer>> res = new ArrayList<>();
        LinkedList<Integer> paths = new LinkedList<>();
        pathSum(root, targetSum, paths, res);
        return res;
    }

    private void pathSum(TreeNode<Integer> root, int targetSum, LinkedList<Integer> paths, List<List<Integer>> res) {
        if (root == null) {
            return;
        }
        paths.addLast(root.val);
        targetSum -= root.val;
        if (root.left == null && root.right == null && targetSum == 0) { // 叶子节点，并且和为targeSum
            res.add(new ArrayList<>(paths));
        }
        pathSum(root.left, targetSum, paths, res);
        pathSum(root.right, targetSum, paths, res);
        paths.removeLast(); // 回溯
    }
}