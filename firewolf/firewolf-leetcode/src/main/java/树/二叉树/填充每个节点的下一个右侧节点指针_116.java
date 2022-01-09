package 树.二叉树;

import utils.TreeUtils;
import utils.TreeUtils.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class 填充每个节点的下一个右侧节点指针_116 {

    public static void main(String[] args) {
        RichTreeNode<Integer> root = TreeUtils.buildBFSRichTTree(null, false, 1, 2, 3, 4, 5, 6, 7);
        RichTreeNode<Integer> connect = new 填充每个节点的下一个右侧节点指针_116().connect(root);
        List<List<Integer>> lists = TreeUtils.travelRichTreeByLevelNextPoint(connect);
        System.out.println(lists);
    }

    public RichTreeNode<Integer> connect(RichTreeNode<Integer> root) {
        if (root == null) {
            return root;
        }
        Queue<RichTreeNode<Integer>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            RichTreeNode<Integer> lastNode = null;
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                RichTreeNode<Integer> node = queue.poll();
                if (i == 0) {
                    lastNode = node;
                } else {
                    lastNode.next = node;
                    lastNode = node;
                }
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        return root;
    }
}