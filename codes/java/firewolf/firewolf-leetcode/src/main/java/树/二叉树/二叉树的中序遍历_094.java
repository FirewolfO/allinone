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
        System.out.println(new 二叉树的中序遍历_094().inorderTraversalMorris(integerTreeNode));
        System.out.println("------------------------------");
        System.out.println(new 二叉树的中序遍历_094().inorderTraversalMorris(integerTreeNode));
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
    public List<Integer> inorderTraversalMorris(TreeNode<Integer> root) {
        List<Integer> result = new ArrayList<>();
        TreeNode<Integer> cur = root; // 当前被处理的的根节点
        while (cur != null) {
            if (cur.left == null) { //没有左孩子了，访问 ---> 处理访问右孩子
                result.add(cur.val);
                cur = cur.right;
            } else {
                TreeNode<Integer> mostRight = cur.left; // 左子树最右边的节点
                while (mostRight.right != null && mostRight.right != cur) { // 找到左子树最右边的那个节点
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) { // 最右节点没有右孩子，让右孩子指向根节点，这样，下次右孩子被访问到之后，就可以访问根节点了
                    mostRight.right = cur;
                    cur = cur.left; // 处理下一个左节点
                } else { // 左子树最右孩子的下个节点是根节点，访问
                    result.add(cur.val);
                    mostRight.right = null;  // 恢复树结构
                    cur = cur.right; // 跟节点访问完了之后，需要处理右孩子
                }
            }
        }
        return result;
    }

}
