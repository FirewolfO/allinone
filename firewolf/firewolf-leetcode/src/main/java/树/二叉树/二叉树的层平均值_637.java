package 树.二叉树;

import utils.TreeUtils;
import utils.TreeUtils.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class 二叉树的层平均值_637 {

    public static void main(String[] args) {
        TreeNode<Integer> integerTreeNode = TreeUtils.buildBSTTree(3, 9, 20, null, null, 15, 7);
        List<Double> doubles = new 二叉树的层平均值_637().averageOfLevels(integerTreeNode);
        System.out.println(doubles);
    }

    public List<Double> averageOfLevels(TreeNode<Integer> root) {
        List<Double> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Queue<TreeNode<Integer>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            double sum = 0d;
            for (int i = 0; i < size; i++) {
                TreeNode<Integer> node = queue.poll();
                sum += node.val;
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            res.add(sum / size);
        }
        return res;
    }
}