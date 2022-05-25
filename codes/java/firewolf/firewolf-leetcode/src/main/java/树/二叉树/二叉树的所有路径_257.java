package 树.二叉树;

import java.util.ArrayList;
import java.util.List;

import static utils.TreeUtils.*;

class 二叉树的所有路径_257 {

    public static void main(String[] args) {
        TreeNode<Integer> root = buildBFSTree(1, 2, 3, null, 5);
        System.out.println(new 二叉树的所有路径_257().binaryTreePaths(root));
    }

    public List<String> binaryTreePaths(TreeNode root) {
        if (root == null) {
            return new ArrayList<>();
        }
        List<String> res = new ArrayList<>();
        if (root.left == null && root.right == null) {
            res.add(root.val + "");
            return res;
        }
        if (root.left != null) {
            for (String one : binaryTreePaths(root.left)) {
                res.add(root.val + "->" + one);
            }
        }
        if (root.right != null) {
            for (String one : binaryTreePaths(root.right)) {
                res.add(root.val + "->" + one);
            }
        }
        return res;
    }
}