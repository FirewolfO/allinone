package 树.二叉树;


import java.util.LinkedList;
import java.util.Queue;

import static utils.TreeUtils.*;

public class 翻转二叉树_226 {

    public static void main(String[] args) {
        TreeNode<Integer> root = buildBSTTree(4, 2, 7, 1, 3, 6, 9);
        System.out.println(showBinaryTreeByLevel(root));

        TreeNode treeNode = new 翻转二叉树_226().invertTreeRecursion(root);
        System.out.println(showBinaryTreeByLevel(treeNode));
    }

    //----------------递归遍历--------------------
    public TreeNode invertTreeRecursion(TreeNode root) {
        if (root == null) {
            return null;
        }
        invertTreeRecursion(root.left);
        invertTreeRecursion(root.right);
        TreeNode tmp = root.left;
        root.left = root.right;
        root.right = tmp;
        return root;
    }


    //-----------------广度优先遍历 ----------------
    public TreeNode invertTreeBST(TreeNode root) {
        if (root == null) {
            return root;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();

        }
        return root;
    }
}
