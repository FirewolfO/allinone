package 树.二叉树;

import utils.TreeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static utils.TreeUtils.TreeNode;

/**
 * 145. 二叉树的后序遍历
 * 给定一个二叉树，返回它的 后序 遍历。
 */
public class 二叉树的后序遍历_145 {

    public static void main(String[] args) {
        TreeNode<Integer> integerTreeNode = TreeUtils.buildBFSTree(new Integer[]{1, null, 3, 4, 5, 6, 7, 8, 9, 10});
        System.out.println(new 二叉树的后序遍历_145().postorderTraversal(integerTreeNode));
        System.out.println(new 二叉树的后序遍历_145().nonRecursionTraversal(integerTreeNode));
    }

    /*****************递归遍历*********************/
    public List<Integer> postorderTraversal(TreeNode<Integer> root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        traversal(root, result);
        return result;
    }


    private void traversal(TreeNode<Integer> root, List<Integer> result) {
        if (root.left != null) {
            traversal(root.left, result);
        }
        if (root.right != null) {
            traversal(root.right, result);
        }
        result.add(root.val);
    }

    /***************非递归遍历：通过中、右、左的方式遍历完了之后反转*****************/
    public List<Integer> nonRecursionTraversal(TreeNode<Integer> root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        Stack<TreeNode<Integer>> stack = new Stack();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode<Integer> pop = stack.pop();
            result.add(0, pop.val); // 通过往前加的方式，实现列表反转
            if (pop.left != null) {
                stack.push(pop.left);
            }
            if (pop.right != null) {
                stack.push(pop.right);
            }
        }
        return result;
    }
}
