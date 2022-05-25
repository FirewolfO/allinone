package 树.二叉搜索树;

import static utils.TreeUtils.*;

class 二叉搜索树的最近公共祖先_235 {
    public TreeNode<Integer> lowestCommonAncestor(TreeNode<Integer> root, TreeNode<Integer> p, TreeNode<Integer> q) {
        if (root == null) {
            return null;
        }
        while (true) {
            if (root.val > p.val && root.val > q.val) { // 比两个节点都大，两个节点在左子树
                root = root.left;
            } else if (root.val < p.val && root.val < q.val) { // 比两个节点都小，两个节点在右子树
                root = root.right;
            } else { // 介于两个节点值之间，是分叉点，也就是公共父节点
                return root;
            }
        }
    }
}