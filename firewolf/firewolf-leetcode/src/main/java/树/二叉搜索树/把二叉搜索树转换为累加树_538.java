package 树.二叉搜索树;

import static utils.TreeUtils.*;

class 把二叉搜索树转换为累加树_538 {

    public static void main(String[] args) {
        TreeNode<Integer> root = buildBFSTree(4, 1, 6, 0, 2, 5, 7, null, null, null, 3, null, null, null, 8);
        TreeNode treeNode = new 把二叉搜索树转换为累加树_538().convertBST(root);
        System.out.println(showBinaryTreeByLevelWithNullInOne(treeNode));
    }


    //---------本题明显可以采用后续遍历，记录之前计算出来的和，然后加上当前节点的值，给当前节点赋值来完成-----
    int preTotal = 0;

    public TreeNode convertBST(TreeNode<Integer> root) {
        dfs(root);
        return root;
    }


    private void dfs(TreeNode<Integer> node) {
        if (node == null) return;
        dfs(node.right);
        preTotal = node.val + preTotal;
        node.val = preTotal;
        dfs(node.left);
    }
}