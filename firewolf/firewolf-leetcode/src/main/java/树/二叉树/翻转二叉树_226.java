package 树.二叉树;


import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import static utils.TreeUtils.*;

public class 翻转二叉树_226 {

    public static void main(String[] args) {
        TreeNode<Integer> root = buildBSTTree(4, 2, 7, 1, 3, 6, 9);
        System.out.println(showBinaryTreeByLevel(root));

        TreeNode treeNode = new 翻转二叉树_226().invertTreeRecursion(root);
        System.out.println(showBinaryTreeByLevel(treeNode));
    }

    //----------------递归遍历--------------------
    public TreeNode invertTreeRecursion(TreeNode root) {
        if (root == null) {
            return null;
        }
        invertTreeRecursion(root.left);
        invertTreeRecursion(root.right);
        TreeNode tmp = root.left;
        root.left = root.right;
        root.right = tmp;
        return root;
    }


    //-----------------广度优先遍历 ----------------
    public TreeNode invertTreeBST(TreeNode root) {
        if (root == null) {
            return root;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            while (size > 0) {
                TreeNode poll = queue.poll();
                TreeNode tmp = poll.left;
                poll.left = poll.right;
                poll.right = tmp;
                if (poll.left != null) {
                    queue.offer(poll.left);
                }
                if (poll.right != null) {
                    queue.offer(poll.right);
                }
                size--;
            }
        }
        return root;
    }

    //------------------深度遍历优先----------------
    public TreeNode invertTreeDST(TreeNode root) {
        if (root == null) {
            return root;
        }
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode pop = stack.pop();
            TreeNode tmp = pop.left;
            pop.left = pop.right;
            pop.right = tmp;
            if (pop.left != null) {
                stack.push(pop.left);
            }
            if (pop.right != null) {
                stack.push(pop.right);
            }
        }
        return root;
    }
}
