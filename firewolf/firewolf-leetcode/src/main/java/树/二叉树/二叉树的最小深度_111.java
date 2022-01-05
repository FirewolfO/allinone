package 树.二叉树;

import utils.TreeUtils;
import utils.TreeUtils.*;

import java.util.LinkedList;
import java.util.Queue;

public class 二叉树的最小深度_111 {

    public static void main(String[] args) {
        TreeNode<Integer> root = TreeUtils.buildBSTTree(2, null, 3, null, 4, null, 5, null, 6);
        System.out.println(new 二叉树的最小深度_111().minDepth(root));
        System.out.println(new 二叉树的最小深度_111().minDepthRecursion(root));
    }

    public int minDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int minDepth = 0;
        while (!queue.isEmpty()) {
            minDepth++;
            int size = queue.size();
            while (size > 0) {
                TreeNode node = queue.poll();
                if (node.left == null && node.right == null) {
                    return minDepth;
                }
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
                size--;
            }
        }
        return minDepth;
    }


    public int minDepthRecursion(TreeNode root) {
        if (root == null) {
            return 0;
        }
        if (root.left == null && root.right == null) { // 左右子树都为空，深度为1
            return 1;
        }
        if (root.left == null) { // 左子树为空，则最小深度在右边
            return minDepthRecursion(root.right) + 1;
        }
        if (root.right == null) { // 右子树为空，最小深度在左子树
            return minDepthRecursion(root.left) + 1;
        }
        return Math.min(minDepthRecursion(root.left), minDepthRecursion(root.right)) + 1;
    }
}
