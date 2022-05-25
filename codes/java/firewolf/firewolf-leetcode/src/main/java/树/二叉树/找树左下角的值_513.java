package 树.二叉树;

import java.util.LinkedList;
import java.util.Queue;

import static utils.TreeUtils.*;

class 找树左下角的值_513 {

    public static void main(String[] args) {
        TreeNode<Integer> root = buildBFSTree(2, 1, 3);
        System.out.println(new 找树左下角的值_513().findBottomLeftValue(root));
        System.out.println(new 找树左下角的值_513().findBottomLeftValueRecursion(root));
    }

    //---------------- 迭代法-----------------
    public int findBottomLeftValue(TreeNode<Integer> root) {
        Queue<TreeNode<Integer>> queue = new LinkedList<>();
        queue.offer(root);
        TreeNode<Integer> leftest = root;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                if (i == 0) {
                    leftest = node;
                }
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        return leftest.val;
    }

    //-------------- 递归法求解--------------------
    int val = 0;
    int maxDeep = -1;

    public int findBottomLeftValueRecursion(TreeNode<Integer> root) {
        findBottomLeftValueRecursion(root, 0);
        return val;
    }

    /**
     * @param root 节点
     * @param deep 节点所处的深度
     * @return
     */
    public void findBottomLeftValueRecursion(TreeNode<Integer> root, int deep) {
        if (root == null) {
            return;
        }
        if (root.left == null && root.right == null) {
            if (deep > maxDeep) {
                maxDeep = deep;
                val = root.val;
            }
        }
        // 必须先左后右，这样，如果左右都存在的时候，右边的deep 不会满足 > maxDeep，得到的值一定是左边的
        if (root.left != null) findBottomLeftValueRecursion(root.left, deep + 1);
        if (root.right != null) findBottomLeftValueRecursion(root.right, deep + 1);
    }
}