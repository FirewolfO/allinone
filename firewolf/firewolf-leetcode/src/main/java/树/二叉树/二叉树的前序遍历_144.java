package 树.二叉树;

import utils.TreeUtils;
import utils.TreeUtils.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 144. 二叉树的前序遍历
 * 给你二叉树的根节点 root ，返回它节点值的 前序 遍历。
 * 描述：
 * Author：liuxing
 * Date：2021-10-26
 */
public class 二叉树的前序遍历_144 {

    public static void main(String[] args) {
        TreeNode<Integer> integerTreeNode = TreeUtils.buildBSTTree(new Integer[]{1, null, 3, 4, 5, 6, 7, 8, 9, 10});
        System.out.println(new 二叉树的前序遍历_144().preorderTraversal(integerTreeNode));
        System.out.println(new 二叉树的前序遍历_144().nonRecursionTraversal(integerTreeNode));
    }

    /*************递归方式 ***************/
    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        traversal(root, result);
        return result;
    }

    private void traversal(TreeNode<Integer> node, List<Integer> result) {
        if (node == null) {
            return;
        }
        result.add(node.val);
        traversal(node.left, result);
        traversal(node.right, result);
    }


    /************* 非递归方式 ****************/
    public List<Integer> nonRecursionTraversal(TreeNode<Integer> root) {
        // 中 左 右
        List<Integer> result = new ArrayList<>();
        Stack<TreeNode<Integer>> stack = new Stack();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode<Integer> pop = stack.pop();
            if(pop == null){
                continue;
            }
            result.add(pop.val);
            stack.push(pop.right);
            stack.push(pop.left);
        }
        return result;
    }

}
