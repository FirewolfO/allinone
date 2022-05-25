package 树.二叉树;

import java.util.Stack;

import static utils.TreeUtils.*;

class 左叶子之和_404 {

    public static void main(String[] args) {
        TreeNode<Integer> root = buildBFSTree(3, 9, 20, null, null, 15, 7);
        System.out.println(new 左叶子之和_404().sumOfLeftLeavesRecursion(root));
        System.out.println(new 左叶子之和_404().sumOfLeftLeaves(root));

    }

    //--------------递归解法----------------
    public int sumOfLeftLeavesRecursion(TreeNode<Integer> root) {
        if (root == null) {
            return 0;
        }
        return sum(root);
    }

    private int sum(TreeNode<Integer> node) {
        if (node == null) {
            return 0;
        }
        int sum = 0;
        if (node.left != null && node.left.left == null && node.left.right == null) { // 是左孩子并且左右孩子都为空
            sum += node.left.val;
        }
        sum += sum(node.left); //左子树的和
        sum += sum(node.right);// 右子树的和
        return sum;
    }

    //------------------------迭代法-------------------
    public int sumOfLeftLeaves(TreeNode<Integer> root) {
        if (root == null) {
            return 0;
        }
        Stack<TreeNode<Integer>> stack = new Stack<>();
        int sum = 0;
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode<Integer> pop = stack.pop();
            if (pop.left != null && pop.left.left == null && pop.left.right == null) {
                sum += pop.left.val;
            }
            if (pop.left != null) {
                stack.push(pop.left);
            }
            if (pop.right != null) {
                stack.push(pop.right);
            }
        }
        return sum;
    }

}