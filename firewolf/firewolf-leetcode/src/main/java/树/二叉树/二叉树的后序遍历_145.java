package 树.二叉树;

import utils.TreeUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import static utils.TreeUtils.TreeNode;

/**
 * 145. 二叉树的后序遍历
 * 给定一个二叉树，返回它的 后序 遍历。
 */
public class 二叉树的后序遍历_145 {

    public static void main(String[] args) {
        TreeNode<Integer> integerTreeNode = TreeUtils.buildBSTTree(new Integer[]{1, null, 3, 4, 5, 6, 7, 8, 9, 10});
        System.out.println(new 二叉树的后序遍历_145().postorderTraversal(integerTreeNode));
        System.out.println(new 二叉树的后序遍历_145().nonRecursionTraversal(integerTreeNode));
    }

    /*****************递归遍历*********************/
    public List<Integer> postorderTraversal(TreeNode<Integer> root) {
        List<Integer> result = new ArrayList<>();
        traversal(root, result);
        return result;
    }


    private void traversal(TreeNode<Integer> root, List<Integer> result) {
        if (root == null) {
            return;
        }
        traversal(root.left, result);
        traversal(root.right, result);
        result.add(root.val);
    }

    /***************非递归遍历*****************/
    public List<Integer> nonRecursionTraversal(TreeNode<Integer> root) {
        Stack<TreeNode<Integer>> stack = new Stack();
        stack.push(root);
        LinkedList<Integer> result = new LinkedList<>();
        while (!stack.isEmpty()) {
            TreeNode<Integer> pop = stack.pop();
            if(pop == null){
                continue;
            }
            result.addFirst(pop.val);
            stack.push(pop.left);
            stack.push(pop.right);
        }
        return result;
    }
}
