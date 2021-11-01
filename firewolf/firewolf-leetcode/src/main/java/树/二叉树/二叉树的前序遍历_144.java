package 树.二叉树;

import utils.TreeUtils;
import utils.TreeUtils.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * Author：liuxing
 * Date：2021-10-26
 */
public class 二叉树的前序遍历_144 {

    public static void main(String[] args) {
        TreeNode<Integer> integerTreeNode = TreeUtils.buildBSTTree(new Integer[]{1, null, 3, 4, 5, 6, 7, 8, 9, 10});
        System.out.println(new 二叉树的前序遍历_144().preorderTraversal(integerTreeNode));
    }

    /*************递归方式 ***************/
    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        traversal(root, result);
        return result;
    }

    private void traversal(TreeNode<Integer> node, List<Integer> result) {
        if (node == null) {
            return;
        }
        result.add(node.val);
        traversal(node.left, result);
        traversal(node.right, result);
    }


    /************* 非递归方式 ****************/

}
