package 树.二叉树;

import utils.TreeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static utils.TreeUtils.TreeNode;

/**
 * 145. 二叉树的后序遍历
 * 给定一个二叉树，返回它的 后序 遍历。
 */
public class 二叉树的后序遍历_145 {

    public static void main(String[] args) {
        TreeNode<Integer> integerTreeNode = TreeUtils.buildBFSTree(new Integer[]{1, null, 3, 4, 5, 6, 7, 8, 9, 10});
        System.out.println(new 二叉树的后序遍历_145().postorderTraversal(integerTreeNode));
        System.out.println(new 二叉树的后序遍历_145().postorderTraversalIterateUnnormal(integerTreeNode));
        System.out.println(new 二叉树的后序遍历_145().postorderTraversalIterate(integerTreeNode));
        System.out.println(new 二叉树的后序遍历_145().postorderTraversalMorris(integerTreeNode));
    }

    /*****************递归遍历*********************/
    public List<Integer> postorderTraversal(TreeNode<Integer> root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        traversal(root, result);
        return result;
    }


    private void traversal(TreeNode<Integer> root, List<Integer> result) {
        if (root.left != null) {
            traversal(root.left, result);
        }
        if (root.right != null) {
            traversal(root.right, result);
        }
        result.add(root.val);
    }

    //------------------迭代遍历--------------------
    public List<Integer> postorderTraversalIterate(TreeNode<Integer> root) {
        List<Integer> result = new ArrayList<>();
        TreeNode<Integer> cur = root;
        TreeNode<Integer> pre = null;
        Stack<TreeNode<Integer>> stack = new Stack();
        while (cur != null || !stack.isEmpty()) {
            if (cur != null) {
                stack.push(cur);
                cur = cur.left;
            } else {
                TreeNode<Integer> peek = stack.peek();
                if (peek.right == null || pre == peek.right) { // peek.right == null --> 叶子节点，直接处理  pre == peek.right---> 上一个处理的是当前节点的右节点，证明该节点的左右节点都已经处理完了，需要处理这个节点了
                    result.add(peek.val);
                    pre = peek; // 记录上一个被遍历的节点
                    stack.pop();
                } else {
                    cur = peek.right;
                }
            }
        }
        return result;
    }


    /***************非常规迭代遍历（更加简洁）：通过中、右、左的方式遍历完了之后反转*****************/
    public List<Integer> postorderTraversalIterateUnnormal(TreeNode<Integer> root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        Stack<TreeNode<Integer>> stack = new Stack();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode<Integer> pop = stack.pop();
            result.add(0, pop.val); // 通过往前加的方式，实现列表反转
            if (pop.left != null) {
                stack.push(pop.left);
            }
            if (pop.right != null) {
                stack.push(pop.right);
            }
        }
        return result;
    }

    //--------------------morris遍历-----------------
    // 核心思想：从左下角开始，每次都逆序访问某个节点的左子树最右边那条边
    public List<Integer> postorderTraversalMorris(TreeNode<Integer> root) {
        List<Integer> result = new ArrayList<>();
        TreeNode<Integer> cur = root;
        while (cur != null) {
            if (cur.left == null) { // 沒有左节点，调到右节点
                cur = cur.right;
            } else {
                TreeNode<Integer> mostRight = cur.left;
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) {
                    mostRight.right = cur;
                    cur = cur.left;
                } else {
                    mostRight.right = null; // 恢复树结构
                    traversalEdge(cur.left, result);
                    cur = cur.right;
                }
            }
        }
        traversalEdge(root, result);
        return result;
    }

    // 逆序遍历一个节点一直到最右节点的节点
    private void traversalEdge(TreeNode<Integer> root, List<Integer> res) {
        int index = res.size();
        while (root != null) {
            res.add(index, root.val);
            root = root.right;
        }
    }

    public static void traversalEdge2(TreeNode<Integer> head, List<Integer> res) {
        TreeNode<Integer> tail = reverseEdge(head);
        TreeNode<Integer> cur = tail;
        while (cur != null) {
            res.add(cur.val);
            cur = cur.right;
        }
        reverseEdge(tail);
    }

    public static TreeNode<Integer> reverseEdge(TreeNode<Integer> from) {
        TreeNode<Integer> pre = null;
        TreeNode<Integer> next = null;
        while (from != null) {
            next = from.right;
            from.right = pre;
            pre = from;
            from = next;
        }
        return pre;
    }
}
