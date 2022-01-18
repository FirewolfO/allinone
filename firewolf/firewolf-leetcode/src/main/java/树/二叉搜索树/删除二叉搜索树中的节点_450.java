package 树.二叉搜索树;

import static utils.TreeUtils.*;

class 删除二叉搜索树中的节点_450 {
    public static void main(String[] args) {
        TreeNode<Integer> root = buildBFSTree(5, 3, 6, 2, 4, null, 7);
        TreeNode<Integer> res = new 删除二叉搜索树中的节点_450().deleteNode(root, 3);
        System.out.println(showBinaryTreeByLevelWithNull(res));
    }

    public TreeNode<Integer> deleteNode(TreeNode<Integer> root, int key) {
        if (root == null) return null;
        if (root.val == key) { //找到节点后删除
            if (root.left == null && root.right == null) return null;
            if (root.left == null) return root.right;
            if (root.right == null) return root.left;
            TreeNode tmp = root.right;
            while (tmp.left != null) {
                tmp = tmp.left;
            }
            tmp.left = root.left;
            return root.right;
        } else if (root.val > key) {
            root.left = deleteNode(root.left, key); //在左节点删除
        } else {
            root.right = deleteNode(root.right, key); // 在右节点删除
        }
        return root;
    }
}