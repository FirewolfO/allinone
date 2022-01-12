package 树.二叉树;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import utils.TreeUtils;
import utils.TreeUtils.*;

public class 二叉树的中序遍历_094 {

    public static void main(String[] args) {
        TreeNode<Integer> integerTreeNode = TreeUtils.buildBFSTree(new Integer[]{1, null, 3, 4, 5, 6, 7, 8, 9, 10});
        System.out.println(new 二叉树的中序遍历_094().inorderTraversalRecursion(integerTreeNode));
        System.out.println(new 二叉树的中序遍历_094().inorderTraversalIterate(integerTreeNode));
        System.out.println(new 二叉树的中序遍历_094().inorderMorris(integerTreeNode));
        System.out.println("------------------------------");
        System.out.println(new 二叉树的中序遍历_094().inorderMorris(integerTreeNode));
    }


    //----------------递归解法-----------------------------
    public List<Integer> inorderTraversalRecursion(TreeNode<Integer> root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        realInorderTraversal(root, res);
        return res;
    }

    public void realInorderTraversal(TreeNode<Integer> root, List<Integer> res) {
        if (root.left != null) {
            realInorderTraversal(root.left, res);
        }
        res.add(root.val);
        if (root.right != null) {
            realInorderTraversal(root.right, res);
        }
    }


    //-------------------迭代解法-------------------
    public List<Integer> inorderTraversalIterate(TreeNode<Integer> root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Stack<TreeNode> stack = new Stack<>();
        TreeNode cur = root;
        while (cur != null || !stack.empty()) {
            if (cur != null) {
                stack.push(cur);
                cur = cur.left;
            } else {
                TreeNode<Integer> node = stack.pop();
                res.add(node.val);
                cur = node.right;
            }
        }
        return res;
    }

    //-------------------morris遍历，空间复杂度O(1)------------------
    public List<Integer> inorderMorris(TreeNode<Integer> root) {
        List<Integer> result = new ArrayList<>();
        TreeNode<Integer> cur = root; // 当前被处理的的根节点
        TreeNode<Integer> pre = null; // 上一个被访问的节点
        while (cur != null) {
            if (cur.left == null || pre.right == cur) { //cur.left == null ---> 没有左子树   pre.right == cur 前一个访问的节点的右孩子是当前节点（左子树访问完了）
                result.add(cur.val);
                if (pre != null && pre.right == cur) {  // 恢复树结构，否在下次遍历就不对了
                    pre.right = null;
                }
                pre = cur;
                cur = cur.right;
                continue;
            }

            // 找 cur 树的左节点的最右节点
            TreeNode<Integer> cur2 = cur.left;
            while (cur2.right != null) {
                cur2 = cur2.right;
            }
            cur2.right = cur; // 让左子树的最右边的节点，指向树的根节点
            cur = cur.left;
        }
        return result;
    }

}
