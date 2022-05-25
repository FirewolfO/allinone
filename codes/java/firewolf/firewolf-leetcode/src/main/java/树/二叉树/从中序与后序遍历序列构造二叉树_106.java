package 树.二叉树;

import static utils.TreeUtils.*;

class 从中序与后序遍历序列构造二叉树_106 {

    public static void main(String[] args) {
        int[] inorder = new int[]{9, 3, 15, 20, 7};
        int[] postorder = new int[]{9, 15, 7, 20, 3};
        System.out.println(new 从中序与后序遍历序列构造二叉树_106().buildTree(inorder, postorder));
    }

    public TreeNode buildTree(int[] inorder, int[] postorder) {
        return buildSubTree(inorder, postorder, 0, 0, inorder.length);
    }

    private TreeNode buildSubTree(int[] inorder, int[] postorder, int iStart, int pStart, int count) {
        TreeNode root = new TreeNode(postorder[pStart + count - 1]); // 构造根节点，后续遍历最后一个元素为根节点
        int index = -1;
        for (int i = iStart; i < iStart + count; i++) { // 根节点在中序遍历中的位置
            if (inorder[i] == postorder[pStart + count - 1]) {
                index = i;
            }
        }

        int leftCount = index - iStart;
        if (leftCount > 0) { // 构造左子树
            int iLeftStart = iStart;
            int pLeftStart = pStart;
            root.left = buildSubTree(inorder, postorder, iLeftStart, pLeftStart, leftCount);
        }

        int rightCount = count - leftCount - 1;
        if (rightCount > 0) { // 构造右子树
            int iRightStart = index + 1;
            int pRightStart = pStart + leftCount;
            root.right = buildSubTree(inorder, postorder, iRightStart, pRightStart, rightCount);
        }
        return root;

    }
}