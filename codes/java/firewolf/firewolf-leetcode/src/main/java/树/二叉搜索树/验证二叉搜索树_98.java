package 树.二叉搜索树;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static utils.TreeUtils.*;

class 验证二叉搜索树_98 {

    public static void main(String[] args) {
        TreeNode<Integer> root = buildBFSTree(2, 1, 3);
        System.out.println(new 验证二叉搜索树_98().isValidBSTForce(root));

        System.out.println(new 验证二叉搜索树_98().isValidBST(root));

        System.out.println(new 验证二叉搜索树_98().isValidBSTIterator(root));
    }

    //----------------- 暴力破解：中序遍历后判断 --------------
    public boolean isValidBSTForce(TreeNode<Integer> root) {
        List<Integer> nums = new ArrayList<>();
        dfs(root, nums);
        for (int i = 0; i < nums.size() - 1; i++) {
            if (nums.get(i) >= nums.get(i + 1)) return false;
        }
        return true;
    }

    private void dfs(TreeNode<Integer> root, List<Integer> nums) {
        if (root == null) return;
        dfs(root.left, nums);
        nums.add(root.val);
        dfs(root.right, nums);
    }

    //------------------递归，中序遍历，判断上一个节点的值是否大于当前节点的值------------
    TreeNode<Integer> pre = null;

    public boolean isValidBST(TreeNode<Integer> root) {
        if (root == null) return true;
        boolean leftValid = isValidBST(root.left);
        if (pre != null && pre.val >= root.val) { //前一个节点更大，直接返回
            return false;
        } else {
            pre = root; // 更新节点
        }
        boolean rightValid = isValidBST(root.right);
        return leftValid && rightValid;
    }

    // -----------------迭代，前序遍历---------------------
    public boolean isValidBSTIterator(TreeNode<Integer> root) {
        if (root == null) {
            return true;
        }
        Stack<TreeNode<Integer>> stack = new Stack<>();
        //前中后
        TreeNode<Integer> cur = root;
        TreeNode<Integer> preNode = null;
        while (cur != null || !stack.isEmpty()) {
            if (cur != null) {
                stack.push(cur);
                cur = cur.left;
            } else {
                cur = stack.pop();
                if (preNode != null && preNode.val >= cur.val) {
                    return false;
                } else {
                    preNode = cur;
                }
                cur = cur.right;
            }
        }
        return true;
    }
}