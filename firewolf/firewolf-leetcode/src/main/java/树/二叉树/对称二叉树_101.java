package 树.二叉树;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import static utils.TreeUtils.*;

public class 对称二叉树_101 {

    public static void main(String[] args) {
        TreeNode<Integer> root = buildBSTTree(1, 2, 2, 3, 4, 4, 3);
        System.out.println(new 对称二叉树_101().isSymmetric(root));
        System.out.println(new 对称二叉树_101().isSymmetricByQueue(root));

    }

    //---------------递归解法------------------
    public boolean isSymmetric(TreeNode root) {
        if (root == null) {
            return true;
        }
        return isSymmetric(root.left, root.right);
    }

    private boolean isSymmetric(TreeNode left, TreeNode right) {
        if (left == null && right == null) return true;
        if (left != null && right == null) return false;
        if (left == null && right != null) return false;
        if (left.val != right.val) return false;
        return isSymmetric(left.right, right.left) && isSymmetric(left.left, right.right);
    }


    //---------------非递归，用队列解决------------
    public boolean isSymmetricByQueue(TreeNode root) {
        if (root == null) {
            return true;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root.left);
        queue.offer(root.right);
        while (!queue.isEmpty()) {
            TreeNode left = queue.poll();
            TreeNode right = queue.poll();
            if (left == null && right == null) continue;
            if ((left != null && right == null) || (left == null && right != null) || (left.val != right.val)) {
                return false;
            }
            queue.offer(left.right);
            queue.offer(right.left);
            queue.offer(left.left);
            queue.offer(right.right);
        }
        return true;
    }

    //---------------非递归，用栈解决------------
    public boolean isSymmetricByStack(TreeNode root) {
        if (root == null) {
            return true;
        }
        Stack<TreeNode> queue = new Stack<>();
        queue.push(root.left);
        queue.push(root.right);
        while (!queue.isEmpty()) {
            TreeNode left = queue.pop();
            TreeNode right = queue.pop();
            if (left == null && right == null) continue;
            if ((left != null && right == null) || (left == null && right != null) || (left.val != right.val)) {
                return false;
            }
            queue.push(left.right);
            queue.push(right.left);
            queue.push(left.left);
            queue.push(right.right);
        }
        return true;
    }
}
