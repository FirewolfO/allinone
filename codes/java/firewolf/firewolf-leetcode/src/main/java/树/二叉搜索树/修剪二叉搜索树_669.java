package 树.二叉搜索树;

import static utils.TreeUtils.*;

class 修剪二叉搜索树_669 {

    public static void main(String[] args) {
        TreeNode<Integer> root = buildBFSTree(1, 0, 2);
        TreeNode<Integer> res = new 修剪二叉搜索树_669().trimBST(root, 1, 2);
        System.out.println(showBinaryTreeByLevelWithNull(res));
    }

    public TreeNode<Integer> trimBST(TreeNode<Integer> root, int low, int high) {
        if (root == null) return null;
        if (root.val > high) return trimBST(root.left, low, high); // root的值大于high，右子树及root没有意义了，全部裁剪掉，同时对左子树进行裁剪；
        if (root.val < low) return trimBST(root.right, low, high); // root的值小于low，左子树及root没有意义了，全部裁剪掉，同时对右子树进行裁剪；
        root.left = trimBST(root.left, low, high);
        root.right = trimBST(root.right, low, high);
        return root;
    }
}