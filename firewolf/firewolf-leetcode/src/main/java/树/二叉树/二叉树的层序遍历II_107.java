package 树.二叉树;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import utils.TreeUtils;
import utils.TreeUtils.*;

class 二叉树的层序遍历II_107 {

    public static void main(String[] args) {
        TreeNode<Integer> integerTreeNode = TreeUtils.buildBSTTree(3, 9, 20, null, null, 15, 7);
        List<List<Integer>> lists = new 二叉树的层序遍历II_107().levelOrderBottom(integerTreeNode);
        System.out.println(lists);
    }

    public List<List<Integer>> levelOrderBottom(TreeNode<Integer> root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            // 遍历某一层
            List<Integer> oneLevel = new ArrayList<>();
            int len = queue.size();
            while (len > 0) {
                TreeNode<Integer> node = queue.poll();
                oneLevel.add(node.val);
                if (node.left != null) { // 加入下一层
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
                len--;
            }
            res.add(0, oneLevel);
        }
        return res;
    }
}