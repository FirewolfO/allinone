package 树.二叉树;

import static utils.TreeUtils.*;

class 从前序与中序遍历序列构造二叉树_105 {

    public static void main(String[] args) {
        int[] preorder = new int[]{3, 9, 20, 15, 7};
        int[] inorder = new int[]{9, 3, 15, 20, 7};
        TreeNode<Integer> root = new 从前序与中序遍历序列构造二叉树_105().buildTree(preorder, inorder);
        System.out.println(root);
    }

    public TreeNode<Integer> buildTree(int[] preorder, int[] inorder) {
        return buildSubTree(preorder, inorder, 0, 0, preorder.length);
    }

    /**
     * 构建一个子树
     *
     * @param preorder 前序遍历数组
     * @param inorder  中序遍历数组
     * @param pStart   子树元素在前序遍历数组中开始位置
     * @param iStart   子树元素在中序遍历数组中开始位置
     * @param count    子树节点个数
     * @return
     */
    private TreeNode<Integer> buildSubTree(int[] preorder, int[] inorder, int pStart, int iStart, int count) {
        TreeNode<Integer> root = new TreeNode<>(preorder[pStart]);//创建根节点
        // 找出根节点在中序遍历的位置，从而得出左右子树中元素个数
        int index = -1;
        for (int i = iStart; i < iStart + count; i++) {
            if (inorder[i] == preorder[pStart]) {
                index = i;
            }
        }

        int leftCount = index - iStart; // 左子树元素个数
        if (leftCount > 0) { // 构造左子树
            int pLeftStart = pStart + 1;
            int iLeftStart = iStart;
            root.left = buildSubTree(preorder, inorder, pLeftStart, iLeftStart, leftCount);
        }

        int rightCount = count - leftCount - 1; // 右子树元素个数
        if (rightCount > 0) {// 构造右子树
            int pRigthStart = pStart + leftCount + 1;
            int iRightStart = index + 1;
            root.right = buildSubTree(preorder, inorder, pRigthStart, iRightStart, rightCount);
        }

        return root;
    }
}