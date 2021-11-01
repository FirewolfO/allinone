package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 描述： 树工具类
 * Author：liuxing
 * Date：2021-10-26
 */
public class TreeUtils {


    /**
     * 广度遍历构建一棵树
     *
     * @param <T>
     * @return
     */
    public static <T> TreeNode<T> buildBSTTree(T... nodeValues) {
        if (nodeValues.length == 0) {
            return null;
        }
        TreeNode<T> root = new TreeNode<>();
        root.val = nodeValues[0];
        createNode(Arrays.asList(root), 1, nodeValues);
        return root;
    }

    private static <T> void createNode(List<TreeNode> parentNodes, int startIndex, T... nodeValues) {
        int i = 0;
        List<TreeNode> nextParent = new ArrayList<>();
        while (i < parentNodes.size() && startIndex <= nodeValues.length - 1) {
            TreeNode curr = parentNodes.get(i);
            if (nodeValues[startIndex] != null) {//不为空
                TreeNode left = new TreeNode(nodeValues[startIndex]);
                nextParent.add(left);
                curr.left = left;
            }
            if (startIndex + 1 <= nodeValues.length - 1) {
                if (nodeValues[startIndex + 1] != null) {
                    TreeNode right = new TreeNode(nodeValues[startIndex + 1]);
                    curr.right = right;
                    nextParent.add(right);
                }
            }
            startIndex += 2;
            i++;
        }
        if (nextParent.size() > 0) {
            createNode(nextParent, startIndex, nodeValues);
        }
    }


    /**
     * 树节点
     */
    public static class TreeNode<T> {
        private T val;
        private TreeNode left;
        private TreeNode right;

        public TreeNode() {
        }

        public TreeNode(T val) {
            this.val = val;
        }

        public TreeNode(T val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
}
