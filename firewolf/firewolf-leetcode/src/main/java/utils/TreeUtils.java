package utils;

import java.util.*;

/**
 * 描述： 树工具类
 * Author：liuxing
 * Date：2021-10-26
 */
public class TreeUtils {


    /***
     * 广度遍历构建二叉树
     * @see TreeUtils#buildBSTTreeCommon(java.lang.Object, java.lang.Object[])
     * @param nodeValues 节点数据
     * @param <T>
     * @return
     */
    public static <T> TreeNode<T> buildBSTTree(T... nodeValues) {
        return buildBSTTreeCommon(null, nodeValues);
    }

    /***
     * 广度遍历构建一棵二叉树
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
     * @param emptyNodeValue 默认空值标志
     * @param nodeValues 所有的节点数据
     * @return
     */
    public static <T> TreeNode<T> buildBSTTreeCommon(Object emptyNodeValue, T... nodeValues) {
        if (nodeValues.length == 0) {
            return null;
        }
        TreeNode<T> root = new TreeNode<>();
        root.val = nodeValues[0];
        createNode(Arrays.asList(root), 1, emptyNodeValue, nodeValues);
        return root;
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

    /***
     * 构建N叉树
     * @see TreeUtils#buildBSTNTreeCommon(java.lang.Object, java.lang.Object[])
     * @param nodeValues 节点数据
     * @param <T>
     * @return
     */
    public static <T> NNode<T> buildBSTNTree(T... nodeValues) {
        return buildBSTNTreeCommon(null, nodeValues);
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
     *             1
     *     /    /    |    \
     *    2    3     4     5
     *       /  \    |    /  \
     *      6    7   8   9   10
     *           |   |   |
     *          11  12  13
     *           |
     *          14
     *
     * @param nodeValues      节点数据
     * @param emptyNodeValue  空节点标志值
     * @param <T>
     * @return
     */
    public static <T> NNode<T> buildBSTNTreeCommon(Object emptyNodeValue, T... nodeValues) {
        if (nodeValues.length == 0) {
            return null;
        }
        NNode<T> root = new NNode<>();
        root.val = nodeValues[0];
        if (nodeValues.length > 2) {
            createNNode(Arrays.asList(root), 1, emptyNodeValue, nodeValues);
        }
        return root;
    }

    /***
     *
     * @param parentNodes      上级节点
     * @param startPreIndex    当前层数据开始位置
     * @param emptyNodeValue   空值
     * @param nodeValues       所有节点值
     * @param <T>
     */
    private static <T> void createNNode(List<NNode> parentNodes, int startPreIndex, Object emptyNodeValue, T... nodeValues) {
        List<NNode> nextParentNodes = new ArrayList<>();
        for (int i = 0; i < parentNodes.size(); i++) {
            List<NNode> children = new ArrayList<>();
            while (startPreIndex < nodeValues.length - 1) {
                startPreIndex++;
                if (nodeValues[startPreIndex] == emptyNodeValue) {
                    break;
                } else {
                    NNode e = new NNode(nodeValues[startPreIndex]);
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
     * 构建可能带有层指针的树
     *  null, true,   1,2,3,4,5,6,7
     *         1
     *      /     \
     *     2   -   3
     *   /  \     / \
     *  4 -  5 - 6 - 7
     * @param emptyNodeValue  空节点值标志
     * @param levelNextPoint  是否需要层指针
     * @param nodeValues      所有节点值
     * @param <T>
     * @return
     */
    public static <T> RichTreeNode<T> buildBSTRichTTree(Object emptyNodeValue, boolean levelNextPoint, T... nodeValues) {
        if (nodeValues.length == 0) {
            return null;
        }
        RichTreeNode<T> root = new RichTreeNode<>();
        root.val = nodeValues[0];
        createRichNode(Arrays.asList(root), 1, emptyNodeValue, levelNextPoint, nodeValues);
        return root;
    }

    /***
     * 通过层指针遍历带层指针的树
     * @param root
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> travelRichTreeByLevelNextPoint(RichTreeNode<T> root) {
        List<List<T>> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Queue<RichTreeNode<T>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                RichTreeNode<T> poll = queue.poll();
                if (i == 0) { // 第一个元素，遍历这一层
                    List<T> oneLevel = new ArrayList<>();
                    RichTreeNode<T> cur = poll;
                    do {
                        oneLevel.add(cur.val);
                        cur = cur.next;
                    } while (cur != null);
                    res.add(oneLevel);
                }
                if (poll.left != null) {
                    queue.offer(poll.left);
                }
                if (poll.right != null) {
                    queue.offer(poll.right);
                }
            }
        }
        return res;

    }

    /***
     * 递归创建节点
     * @param parentNodes     父节点集合
     * @param startIndex      当前层节点数据开始的索引
     * @param emptyNodeValue  空值标志
     * @param levelNextPoint  是否有层到下个节点的指针
     * @param nodeValues      所有的节点数据
     * @param <T>
     */
    private static <T> void createRichNode(List<RichTreeNode<T>> parentNodes, int startIndex, Object emptyNodeValue, boolean levelNextPoint, T[] nodeValues) {
        if (levelNextPoint) { // 连接节点之间的next指针
            for (int i = 1; i < parentNodes.size(); i++) {
                parentNodes.get(i - 1).next = parentNodes.get(i);
            }
        }
        int i = 0;
        List<RichTreeNode<T>> nextParent = new ArrayList<>();
        while (i < parentNodes.size() && startIndex <= nodeValues.length - 1) {
            RichTreeNode<T> curr = parentNodes.get(i);
            if (nodeValues[startIndex] != emptyNodeValue) {//不为空
                RichTreeNode<T> left = new RichTreeNode<T>(nodeValues[startIndex]);
                nextParent.add(left);
                curr.left = left;
            }
            if (startIndex + 1 <= nodeValues.length - 1) {
                if (nodeValues[startIndex + 1] != emptyNodeValue) {
                    RichTreeNode<T> right = new RichTreeNode<T>(nodeValues[startIndex + 1]);
                    curr.right = right;
                    nextParent.add(right);
                }
            }
            startIndex += 2;
            i++;
        }
        if (nextParent.size() > 0) {
            createRichNode(nextParent, startIndex, emptyNodeValue, levelNextPoint, nodeValues);
        }
    }


    /***
     * N叉树节点
     * @param <T>
     */
    public static class NNode<T> {
        public T val;
        public List<NNode<T>> children;

        public NNode() {
        }

        public NNode(T val) {
            this.val = val;
        }

        public NNode(T val, TreeNode left, List<NNode<T>> children) {
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

    /***
     * 较丰富的二叉树节点，有到同层下个节点的指针
     * @param <T>
     */
    public static class RichTreeNode<T> {
        public T val;
        public RichTreeNode<T> left;
        public RichTreeNode<T> right;
        public RichTreeNode<T> next;

        public RichTreeNode() {
        }

        public RichTreeNode(T val) {
            this.val = val;
        }

        public RichTreeNode(T val, RichTreeNode<T> left, RichTreeNode<T> right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }


        public RichTreeNode(T val, RichTreeNode<T> left, RichTreeNode<T> right, RichTreeNode<T> next) {
            this.val = val;
            this.left = left;
            this.right = right;
            this.next = next;
        }

        @Override
        public String toString() {
            return "RichTreeNode{" +
                    "val=" + val +
                    ", left=" + left +
                    ", right=" + right +
                    ", next=" + (next == null ? next : "#") +
                    '}';
        }
    }

    /***
     *  二叉树节点
     */
    public static class TreeNode<T> {
        public T val;
        public TreeNode<T> left;
        public TreeNode<T> right;

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
