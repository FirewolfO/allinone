package 树.二叉树;

import static utils.TreeUtils.*;

public class 完全二叉树的节点个数_222 {

    public static void main(String[] args) {
        TreeNode<Integer> root = buildBSTTree(1, 2, 3, 4, 5, 6);
        System.out.println(new 完全二叉树的节点个数_222().countNodes(root));
        System.out.println(new 完全二叉树的节点个数_222().countNodesByRecursion(root));
    }


    //------------------------- 递归暴力破解，无视完全二字-------------------------
    public int countNodes(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return countNodes(root.left) + countNodes(root.right) + 1;
    }


    //------------------------将完全二叉树化解成多个满二叉树，对于h层的满二叉树，节点个数为2<h-1个--------------------------
    // 详解：https://programmercarl.com/0222.%E5%AE%8C%E5%85%A8%E4%BA%8C%E5%8F%89%E6%A0%91%E7%9A%84%E8%8A%82%E7%82%B9%E4%B8%AA%E6%95%B0.html#c
    public int countNodesByRecursion(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int leftLevel = 0;
        int rightLevel = 0;
        TreeNode left = root.left;
        TreeNode right = root.right;
        while (left != null) {
            leftLevel++;
            left = left.left;
        }
        while (right != null) {
            rightLevel++;
            right = right.right;
        }
        if (leftLevel == rightLevel) {// 最左路径层次和最右路径层次一样，是一颗满二叉树
            return (2 << leftLevel) - 1;
        }
        return countNodesByRecursion(root.left) + countNodesByRecursion(root.right) + 1;
    }
}
