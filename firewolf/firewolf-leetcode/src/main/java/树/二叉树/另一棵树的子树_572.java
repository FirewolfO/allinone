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
class 另一棵树的子树_572 {

    public static void main(String[] args) {
        TreeNode<Integer> root = buildBSTTree(4, 1, null, null, 2);
        TreeNode<Integer> subRoot = buildBSTTree(1, null, 4, 2);
        System.out.println(new 另一棵树的子树_572().isSubtree(root, subRoot));

        System.out.println(new 另一棵树的子树_572().isSubtreeRecursion(root, subRoot));
        System.out.println("xxx");
    }


    //----------递归暴力破解判断------------
    public boolean isSubtreeRecursion(TreeNode root, TreeNode subRoot) { // 把主树的各个节点当做跟节点来对比
        if (root == null) {
            return false;
        }
        return check(root, subRoot) || isSubtreeRecursion(root.left, subRoot) || isSubtreeRecursion(root.right, subRoot);
    }

    private boolean check(TreeNode node, TreeNode subNode) { //两棵树的节点逐个对比
        if (node == null && subNode == null) {
            return true;
        }
        if (node != null || subNode != null || node.val != subNode.val) {
            return false;
        }
        return check(node.left, subNode.left) && check(node.right, subNode.right);
    }


    //---------前序遍历构建成字符串，然后判断是否包含-----------
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

    //-------前序遍历构建成数组后是用KMP判断-----------------------
}