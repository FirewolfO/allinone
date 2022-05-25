package 树.二叉搜索树;

import java.util.Stack;

import static utils.TreeUtils.*;

class 二叉搜索树中的搜索_700 {

    public static void main(String[] args) {
        TreeNode<Integer> root = buildBFSTree(4, 2, 7, 1, 3);
        TreeNode result = new 二叉搜索树中的搜索_700().searchBST(root, 2);
        System.out.println(showBinaryTreeByLevelWithNull(result));


        TreeNode result2 = new 二叉搜索树中的搜索_700().searchBSTIterate(root, 2);
        System.out.println(showBinaryTreeByLevelWithNull(result2));
    }

    //-----------递归法------------
    public TreeNode searchBST(TreeNode<Integer> root, int val) {
        if (root == null) {
            return null;
        }
        if (root.val == val) {
            return root;
        } else if (root.val > val) {
            return searchBST(root.left, val);
        } else {
            return searchBST(root.right, val);
        }
    }

    //-----------迭代法-----------
    public TreeNode searchBSTIterate(TreeNode<Integer> root, int val) {
        if (root == null) {
            return null;
        }
        Stack<TreeNode<Integer>> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode<Integer> pop = stack.pop();
            if (pop.val == val) {
                return pop;
            }
            if (pop.val < val && pop.right != null) {
                stack.push(pop.right);
            }
            if (pop.val > val && pop.left != null) {
                stack.push(pop.left);
            }
        }
        return null;
    }

    //----------------迭代优化，不使用栈------------
    public TreeNode searchBSTIterateWithoutStack(TreeNode<Integer> root, int val) {
        while (root != null) {
            if (root.val == val) {
                return root;
            } else if (root.val > val) {
                root = root.left;
            } else {
                root = root.right;
            }
        }
        return null;
    }
}