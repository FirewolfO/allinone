package 树.二叉树;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import utils.TreeUtils;
import utils.TreeUtils.*;

public class 二叉树的中序遍历_094 {

    public static void main(String[] args) {
        TreeNode<Integer> integerTreeNode = TreeUtils.buildBFSTree(new Integer[]{1, null, 3, 4, 5, 6, 7, 8, 9, 10});
        System.out.println(new 二叉树的中序遍历_094().inorderTraversal(integerTreeNode));
        System.out.println(new 二叉树的中序遍历_094().nonRecursionInorderTraversal(integerTreeNode));
    }


    //----------------递归解法-----------------------------
    public List<Integer> inorderTraversal(TreeNode<Integer> root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        realInorderTraversal(root, res);
        return res;
    }

    public void realInorderTraversal(TreeNode<Integer> root, List<Integer> res) {
        if (root.left != null) {
            realInorderTraversal(root.left, res);
        }
        res.add(root.val);
        if (root.right != null) {
            realInorderTraversal(root.right, res);
        }
    }


    //-------------------非递归解法-------------------
    public List<Integer> nonRecursionInorderTraversal(TreeNode<Integer> root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Stack<TreeNode> stack = new Stack<>();
        TreeNode cur = root;
        while (cur != null || !stack.empty()) {
            if (cur != null) {
                stack.push(cur);
                cur = cur.left;
            } else {
                TreeNode<Integer> node = stack.pop();
                res.add(node.val);
                cur = node.right;
            }
        }
        return res;
    }

}
