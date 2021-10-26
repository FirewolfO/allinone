package utils;

/**
 * 描述： 树工具类
 * Author：liuxing
 * Date：2021-10-26
 */
public class TreeUtils {


    /**
     * 构建一棵树
     *
     * @param <T>
     * @return
     */
    public static <T> TreeNode<T> buildTree(T... nodeValues) {
        return null;
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
