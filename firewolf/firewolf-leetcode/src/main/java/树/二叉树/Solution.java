package 树.二叉树;

import static utils.TreeUtils.*;

/**
 * Definition for a binary tree node.
 * public class TreeNode {
 * int val;
 * TreeNode left;
 * TreeNode right;
 * TreeNode() {}
 * TreeNode(int val) { this.val = val; }
 * TreeNode(int val, TreeNode left, TreeNode right) {
 * this.val = val;
 * this.left = left;
 * this.right = right;
 * }
 * }
 */
class Solution {

    public static void main(String[] args) {
        TreeNode<Integer> root = buildBSTTree(4, 1, null, null, 2);
        TreeNode<Integer> subRoot = buildBSTTree(1, null, 4, 2);
        System.out.println(new Solution().isSubtree(root, subRoot));
        System.out.println("xxx");
    }


    //---------谦虚遍历构建成字符串，然后判断-----------
    public boolean isSubtree(TreeNode root, TreeNode subRoot) {
        String subTreeStr = treeStr(subRoot);
        String rootTreeStr = treeStr(root);
        return rootTreeStr.contains(subTreeStr);
    }

    // 需要用前序遍历构建
    private String treeStr(TreeNode root) {
        String leftStr = root.left == null ? "LNull" : treeStr(root.left);
        String rightStr = root.right == null ? "RNull" : treeStr(root.right);
        return "," + root.val + "," + leftStr + "," + rightStr + ",";
    }
}