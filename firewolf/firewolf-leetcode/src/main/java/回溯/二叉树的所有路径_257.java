package 回溯;

import java.util.ArrayList;
import java.util.List;

import static utils.TreeUtils.*;

class 二叉树的所有路径_257 {

    public static void main(String[] args) {
        TreeNode<Integer> root = buildBFSTree(1, 2, 3, null, 5);
        System.out.println(new 二叉树的所有路径_257().binaryTreePaths(root));
    }

    //---------------------------- 回溯解法-----------------------
    public List<String> binaryTreePaths(TreeNode<Integer> root) {
        List<String> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        List<Integer> paths = new ArrayList<>();
        binaryTreePaths(root, paths, res);
        return res;
    }

    private void binaryTreePaths(TreeNode<Integer> node, List<Integer> paths, List<String> res) {
        paths.add(node.val);
        if (node.left == null && node.right == null) { //当前节点是叶子节点，开始收集结果
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < paths.size() - 1; i++) {
                sb.append(paths.get(i)).append("->");
            }
            sb.append(paths.get(paths.size() - 1));
            res.add(sb.toString());
            return;
        }
        if (node.left != null) {
            binaryTreePaths(node.left, paths, res);
            paths.remove(paths.size() - 1); //回溯
        }
        if (node.right != null) {
            binaryTreePaths(node.right, paths, res);
            paths.remove(paths.size() - 1); //回溯
        }
    }
}