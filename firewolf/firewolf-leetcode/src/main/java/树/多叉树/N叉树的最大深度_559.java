package 树.多叉树;

import java.util.LinkedList;
import java.util.Queue;

import static utils.TreeUtils.*;

class N叉树的最大深度_559 {

    public static void main(String[] args) {
        NNode<Integer> root = buildBSTNTree(1, null, 3, 2, 4, null, 5, 6);
        System.out.println(new N叉树的最大深度_559().maxDepthRecursion(root));
        System.out.println(new N叉树的最大深度_559().maxDepth(root));
    }


    //----------------非递归-----------------------
    public int maxDepth(NNode<Integer> root) {
        if (root == null) {
            return 0;
        }
        Queue<NNode<Integer>> queue = new LinkedList<>();
        queue.offer(root);
        int maxDep = 0;
        while (!queue.isEmpty()) {
            maxDep++;
            int size = queue.size();
            while (size > 0) {
                NNode<Integer> poll = queue.poll();
                for (NNode<Integer> n : poll.children) {
                    if (n != null) {
                        queue.offer(n);
                    }
                }
                size--;
            }
        }
        return maxDep;
    }


    //-------------------递归解法---------------------
    public int maxDepthRecursion(NNode<Integer> root) {
        if (root == null) {
            return 0;
        }
        int maxDepth = 0;
        for (int i = 0; i < root.children.size(); i++) {
            maxDepth = Math.max(maxDepth, maxDepthRecursion(root.children.get(i)));
        }
        return maxDepth + 1;
    }
}