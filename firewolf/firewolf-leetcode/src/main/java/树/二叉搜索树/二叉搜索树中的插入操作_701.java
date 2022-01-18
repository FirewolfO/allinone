package 树.二叉搜索树;

import static utils.TreeUtils.*;

class 二叉搜索树中的插入操作_701 {

    public static void main(String[] args) {
        TreeNode<Integer> root = buildBFSTree(4, 2, 7, 1, 3);
        TreeNode<Integer> result = new 二叉搜索树中的插入操作_701().insertIntoBST(root, 5);
        System.out.println(showBinaryTreeByLevelWithNull(result));

    }

    public TreeNode<Integer> insertIntoBST(TreeNode<Integer> root, int val) {
        if (root == null) return new TreeNode(val);
        TreeNode<Integer> tmp = root;
        while (tmp != null) {
            if (tmp.val > val) {
                if (tmp.left == null) {
                    tmp.left = new TreeNode(val);
                    break;
                } else {
                    tmp = tmp.left;
                }
            } else {
                if (tmp.right == null) {
                    tmp.right = new TreeNode(val);
                    break;
                } else {
                    tmp = tmp.right;
                }
            }
        }
        return root;
    }
}