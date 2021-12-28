package 树.二叉树;

import utils.TreeUtils;
import utils.TreeUtils.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class 二叉树的层序遍历_102 {

    public static void main(String[] args) {
        TreeNode<Integer> integerTreeNode = TreeUtils.buildBSTTree(3, 9, 20, null, null, 15, 7);
        List<List<Integer>> lists = new 二叉树的层序遍历_102().levelOrder(integerTreeNode);
        System.out.println(lists);
    }

    public List<List<Integer>> levelOrder(TreeNode<Integer> root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Queue<TreeNode<Integer>> que = new LinkedList<>();
        que.offer(root);
        while (!que.isEmpty()) {
            int len = que.size();
            List<Integer> oneLevel = new ArrayList<>();
            while (len > 0) {
                TreeNode<Integer> node = que.poll();
                oneLevel.add(node.val);
                if (node.left != null) {
                    que.offer(node.left);
                }
                if (node.right != null) {
                    que.offer(node.right);
                }
                len--;
            }
            res.add(oneLevel);
        }
        return res;
    }
}