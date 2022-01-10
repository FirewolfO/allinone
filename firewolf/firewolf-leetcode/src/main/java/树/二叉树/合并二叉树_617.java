package 树.二叉树;

import static utils.TreeUtils.*;

class 合并二叉树_617 {

    public static void main(String[] args) {
        TreeNode<Integer> root1 = buildBFSTree(1, 3, 2, 5);
        TreeNode<Integer> root2 = buildBFSTree(2, 1, 3, null, 4, null, 7);
        TreeNode treeNode = new 合并二叉树_617().mergeTrees(root1, root2);
        System.out.println(showBinaryTreeByLevelWithNull(treeNode));
    }

    public TreeNode mergeTrees(TreeNode<Integer> root1, TreeNode<Integer> root2) {
        if (root1 == null && root2 == null) { // 两个树都是空，则直接返回
            return null;
        }
        if (root1 == null) { //第一个树为空，返回第二个树
            return root2;
        }
        if (root2 == null) { //第二个树为空，返回第一个树
            return root1;
        }

        // 都不为空的时候，节点值为两个值得和，同时需要计算该节点的左右子树
        TreeNode<Integer> root = new TreeNode();
        root.val = root1.val + root2.val;
        root.left = mergeTrees(root1.left, root2.left);
        root.right = mergeTrees(root1.right, root2.right);
        return root;
    }
}