package 树.二叉树;

import static utils.TreeUtils.*;

class 二叉树的最近公共祖先_236 {

    public static void main(String[] args) {
        TreeNode<Integer> root = buildBFSTree(3, 5, 1, 6, 2, 0, 8, null, null, 7, 4);
    }

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) return root;
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        if (left != null && right != null) return root; // 左右孩子都不为空的时候，就是公共父节点
        if (left != null) return left; // 返回不为空的这个节点
        return right;
    }
}