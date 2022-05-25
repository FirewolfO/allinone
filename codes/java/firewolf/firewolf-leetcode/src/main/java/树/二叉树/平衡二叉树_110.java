package 树.二叉树;

import static utils.TreeUtils.*;

class 平衡二叉树_110 {
    public boolean isBalanced(TreeNode root) {
        return hight(root) == -1 ? false : true;
    }

    //-------------------递归计算高度，-1表示左右两棵树的高度差大于1------------------------
    private int hight(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int leftH = hight(root.left);
        if (leftH == -1) {
            return -1;
        }
        int rightH = hight(root.right);
        if (rightH == -1) {
            return -1;
        }
        if (Math.abs(leftH - rightH) > 1) { // -1 表示高度差大于1
            return -1;
        }
        return Math.max(leftH, rightH) + 1;
    }
}