package 回溯;

import java.util.ArrayList;
import java.util.LinkedList;
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
        LinkedList<Integer> paths = new LinkedList<>();
        binaryTreePaths(root, paths, res);
        return res;
    }

    private void binaryTreePaths(TreeNode<Integer> node, LinkedList<Integer> paths, List<String> res) {
        if (node == null) {
            return;
        }
        paths.add(node.val);
        if (node.left == null && node.right == null) { //当前节点是叶子节点，开始收集结果
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < paths.size() - 1; i++) {
                sb.append(paths.get(i)).append("->");
            }
            sb.append(paths.get(paths.size() - 1));
            res.add(sb.toString());
        }
        binaryTreePaths(node.left, paths, res);
        binaryTreePaths(node.right, paths, res);
        paths.removeLast();
    }
}