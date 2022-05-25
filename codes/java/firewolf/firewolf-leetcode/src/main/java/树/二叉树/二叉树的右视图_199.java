package 树.二叉树;

import utils.TreeUtils;
import utils.TreeUtils.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class 二叉树的右视图_199 {

    public static void main(String[] args) {
        TreeNode<Integer> integerTreeNode = TreeUtils.buildBFSTree(1, 2, 3, null, 5, null, 4);
        List<Integer> integers = new 二叉树的右视图_199().rightSideView(integerTreeNode);
        System.out.println(integers);
    }

    public List<Integer> rightSideView(TreeNode<Integer> root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Queue<TreeNode<Integer>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int len = queue.size();
            while (len > 1) {
                getNodeVal(queue);
                len--;
            }
            res.add(getNodeVal(queue)); //收集每层最后一个元素
        }
        return res;
    }

    private int getNodeVal(Queue<TreeNode<Integer>> queue) {
        TreeNode<Integer> node = queue.poll();
        if (node.left != null) {
            queue.offer(node.left);
        }
        if (node.right != null) {
            queue.offer(node.right);
        }
        return node.val;
    }
}