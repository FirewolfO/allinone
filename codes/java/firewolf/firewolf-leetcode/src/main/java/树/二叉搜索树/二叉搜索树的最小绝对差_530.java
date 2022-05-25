package 树.二叉搜索树;

import java.util.Stack;

import static utils.TreeUtils.*;

class 二叉搜索树的最小绝对差_530 {
    public static void main(String[] args) {
        TreeNode<Integer> root = buildBFSTree(4, 2, 6, 1, 3);
        System.out.println(new 二叉搜索树的最小绝对差_530().getMinimumDifference(root));
        System.out.println(new 二叉搜索树的最小绝对差_530().getMinimumDifferenceRecursion(root));
    }

    /***
     * 由于是二叉搜索树，所以，差值绝对值最小的，一定是中序遍历相邻的元素
     * @param root
     * @return
     */
    //----------------迭代解法-----------
    public int getMinimumDifference(TreeNode<Integer> root) {
        Stack<TreeNode<Integer>> stack = new Stack<>();
        TreeNode<Integer> cur = root;
        TreeNode<Integer> pre = null;
        int min = 100000;
        while (cur != null || !stack.isEmpty()) {
            if (cur != null) {
                stack.push(cur);
                cur = cur.left;
            } else {
                TreeNode<Integer> pop = stack.pop();
                if (pre != null && pop.val - pre.val < min) {//遇到更小的绝对值，更新
                    min = pop.val - pre.val;
                }
                cur = pop.right;
                pre = pop;
            }
        }
        return min;
    }


    //----------- 递归解法-----
    int preVal = -1; // 上一个元素的值
    int min = 1000000; // 结果

    public int getMinimumDifferenceRecursion(TreeNode<Integer> root) {
        dfs(root);
        return min;
    }

    private void dfs(TreeNode<Integer> root) {
        if (root == null) return;
        dfs(root.left);
        if (preVal != -1) {
            min = Math.min(min, root.val - preVal);
        }
        preVal = root.val;
        dfs(root.right);
    }
}