package 树.二叉搜索树;

import java.util.ArrayList;
import java.util.List;

import static utils.TreeUtils.*;

class Solution {

    public static void main(String[] args) {
        TreeNode<Integer> root = buildBFSTree(3, 1, 4, null, 2);
        System.out.println(new Solution().kthLargest(root, 1));
    }

    public int kthLargest(TreeNode<Integer> root, int k) {
        List<Integer> vals = new ArrayList<>();
        dfs(root, vals, k);
        return vals.get(vals.size() - 1);
    }

    private boolean dfs(TreeNode<Integer> root, List<Integer> vals, int k) {
        if (root.right != null) {
            if (dfs(root.right, vals, k)) return true;
        }
        vals.add(root.val);
        if (vals.size() == k) {
            return true;
        }
        if (root.left != null) {
            if (dfs(root.left, vals, k)) return true;
        }
        return false;
    }
}