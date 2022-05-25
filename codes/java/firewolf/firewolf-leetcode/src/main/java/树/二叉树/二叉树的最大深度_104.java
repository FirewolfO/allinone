package 树.二叉树;

import utils.TreeUtils;
import utils.TreeUtils.*;

import java.util.LinkedList;
import java.util.Queue;

public class 二叉树的最大深度_104 {
    public static void main(String[] args) {
        TreeNode<Integer> root = TreeUtils.buildBFSTree(3, 9, 20, null, null, 15, 7);
        int maxDepth = new 二叉树的最大深度_104().maxDepth(root);
        System.out.println(maxDepth);
    }

    public int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int deepth = 0;
        while (!queue.isEmpty()) {
            deepth++;
            int size = queue.size();
            while (size > 0) {
                TreeNode node = queue.poll();
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
                size--;
            }
        }
        return deepth;
    }


    //-------------递归解法---------
    public int maxDepthRecursion(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return Math.max(maxDepthRecursion(root.left), maxDepthRecursion(root.right)) + 1;
    }
}
