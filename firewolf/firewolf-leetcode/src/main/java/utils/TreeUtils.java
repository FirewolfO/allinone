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


    public static void main(String[] args) {
        TreeNode<Integer> integerTreeNode = buildBSTTree(1, 2, 3, null, 4, null, null, 5);

    }

    /***
     * 广度遍历构建一棵二叉树，没有的节点用null表示
     * 如： [1,2,3,null,4,null,null,5]
     *          1
     *        /   \
     *       2     3
     *        \
     *         4
     *        /
     *       5
     *
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
        createNode(Arrays.asList(root), 1, null, nodeValues);
        return root;
    }


    /***
     * 构建N叉树，各个N节点组之间使用null区分，如
     * [1,null,3,2,4,null,5,6] 的构建结果为：
     *         1
     *       / | \
     *      3  2  4
     *     / \
     *    5   6
     * 再如：[1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,null,13,null,null,14]，构建结果为：
     *                           1
     *                   /    /    |    \
     *                  2    3     4     5
     *                     /  \    |    /  \
     *                    6    7   8   9   10
     *                         |   |   |
     *                         11  12  13
     *                         |
     *                         14
     *
     * @param nodeValues
     * @param <T>
     * @return
     */
    public static <T> Node<T> buildBSTNTree(T... nodeValues) {
        if (nodeValues.length == 0) {
            return null;
        }
        Node<T> root = new Node<>();
        root.val = nodeValues[0];
        if (nodeValues.length > 2) {
            createNNode(Arrays.asList(root), 1, null, nodeValues);
        }
        return root;
    }


    private static <T> void createNNode(List<Node> parentNodes, int startPreIndex, Object emptyNodeValue, T... nodeValues) {
        List<Node> nextParentNodes = new ArrayList<>();
        for (int i = 0; i < parentNodes.size(); i++) {
            List<Node> children = new ArrayList<>();
            while (startPreIndex < nodeValues.length - 1) {
                startPreIndex++;
                if (nodeValues[startPreIndex] == emptyNodeValue) {
                    break;
                } else {
                    Node e = new Node(nodeValues[startPreIndex]);
                    children.add(e);
                    nextParentNodes.add(e);
                }
            }
            if (children.size() > 0) {
                parentNodes.get(i).children = children;
            }
        }
        if (nextParentNodes.size() > 0) {
            createNNode(nextParentNodes, startPreIndex, emptyNodeValue, nodeValues);
        }
    }


    /***
     * @param parentNodes 父节点层
     * @param startIndex 当前层元素的开始索引
     * @param emptyNodeValue 默认的空节点值
     * @param nodeValues 所有的节点值数组
     * @param <T>
     */
    private static <T> void createNode(List<TreeNode> parentNodes, int startIndex, Object emptyNodeValue, T... nodeValues) {
        int i = 0;
        List<TreeNode> nextParent = new ArrayList<>();
        while (i < parentNodes.size() && startIndex <= nodeValues.length - 1) {
            TreeNode curr = parentNodes.get(i);
            if (nodeValues[startIndex] != emptyNodeValue) {//不为空
                TreeNode left = new TreeNode(nodeValues[startIndex]);
                nextParent.add(left);
                curr.left = left;
            }
            if (startIndex + 1 <= nodeValues.length - 1) {
                if (nodeValues[startIndex + 1] != emptyNodeValue) {
                    TreeNode right = new TreeNode(nodeValues[startIndex + 1]);
                    curr.right = right;
                    nextParent.add(right);
                }
            }
            startIndex += 2;
            i++;
        }
        if (nextParent.size() > 0) {
            createNode(nextParent, startIndex, emptyNodeValue, nodeValues);
        }
    }


    public static class Node<T> {
        public T val;
        public List<Node> children;

        public Node() {
        }

        public Node(T val) {
            this.val = val;
        }

        public Node(T val, TreeNode left, List<Node> children) {
            this.val = val;
            this.children = children;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "val=" + val +
                    ", children=" + children +
                    '}';
        }
    }

    /**
     * 树节点
     */
    public static class TreeNode<T> {
        public T val;
        public TreeNode left;
        public TreeNode right;

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

        @Override
        public String toString() {
            return "TreeNode{" +
                    "val=" + val +
                    ", left=" + left +
                    ", right=" + right +
                    '}';
        }
    }
}
