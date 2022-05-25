package 树.二叉树;

import static utils.TreeUtils.*;

public class 相同的树_100 {

    public static void main(String[] args) {
        TreeNode<Integer> p = buildBFSTree(1, 2, 3);
        TreeNode<Integer> q = buildBFSTree(1, 2, 3);
        System.out.println(new 相同的树_100().isSameTree(p, q));

    }

    public boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && q == null) { // 两个节点都为null，相同
            return true;
        }
        if (p == null || q == null || p.val != q.val) { // 有一个节点为空或者值不同，不相同
            return false;
        }
        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right); // 需要满足左树相同且右树相同
    }
}
