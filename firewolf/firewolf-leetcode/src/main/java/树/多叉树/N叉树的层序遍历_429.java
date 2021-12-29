package 树.多叉树;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import utils.TreeUtils;
import utils.TreeUtils.*;

class N叉树的层序遍历_429 {

    public static void main(String[] args) {
        NNode<Integer> nodes = TreeUtils.buildBSTNTree(1, null, 2, 3, 4, 5, null, null, 6, 7, null, 8, null, 9, 10, null, null, 11, null, 12, null, 13, null, null, 14);
        List<List<Integer>> lists = new N叉树的层序遍历_429().levelOrder(nodes);
        System.out.println(lists);
    }

    public List<List<Integer>> levelOrder(NNode<Integer> root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Queue<NNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> oneLevel = new ArrayList<>();
            while (size > 0) {
                NNode<Integer> node = queue.poll();
                oneLevel.add(node.val);
                if (node.children != null) { //遍历所有的子节点
                    for (NNode n : node.children) {
                        if (n != null) {
                            queue.offer(n);
                        }
                    }
                }
                size--;
            }
            res.add(oneLevel);
        }
        return res;
    }
}