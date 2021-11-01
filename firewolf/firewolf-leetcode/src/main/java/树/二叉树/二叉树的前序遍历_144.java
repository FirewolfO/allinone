package 树.二叉树;

import utils.TreeUtils;
import utils.TreeUtils.TreeNode;

import java.util.List;

/**
 * 描述：
 * Author：liuxing
 * Date：2021-10-26
 */
public class 二叉树的前序遍历_144 {

    public static void main(String[] args) {
        TreeNode<Integer> integerTreeNode = TreeUtils.buildBSTTree(new Integer[]{1,null,3,4,5,6,7,8,9,10});
        System.out.println(integerTreeNode);
    }

    public List<Integer> preorderTraversal(TreeNode root) {
        return null;
    }


}
