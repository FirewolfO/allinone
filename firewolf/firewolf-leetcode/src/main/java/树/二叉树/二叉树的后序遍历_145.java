package 树.二叉树;

import utils.TreeUtils;

import java.util.ArrayList;
import java.util.List;

import static utils.TreeUtils.TreeNode;

public class 二叉树的后序遍历_145 {

    public static void main(String[] args) {
        TreeNode<Integer> integerTreeNode = TreeUtils.buildBSTTree(new Integer[]{1, null, 3, 4, 5, 6, 7, 8, 9, 10});
        System.out.println(new 二叉树的后序遍历_145().postorderTraversal(integerTreeNode));
    }

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


}
