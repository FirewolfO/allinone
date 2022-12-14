package 树.二叉搜索树;

import java.util.*;

import static utils.TreeUtils.*;

class 二叉搜索树中的众数_501 {

    public static void main(String[] args) {
        TreeNode<Integer> root = buildBFSTree(1, null, 2, 2);
        System.out.println(Arrays.toString(new 二叉搜索树中的众数_501().findModeIterate(root)));
        System.out.println(Arrays.toString(new 二叉搜索树中的众数_501().findMode(root)));
        System.out.println(Arrays.toString(new 二叉搜索树中的众数_501().findModeMorris(root)));
    }


    //--------------------递归---------------------
    List<Integer> maxList2 = new ArrayList<>();
    int maxCount2; //最大的个数
    int curVal2; // 当前被统计的值
    int curCount2; // 当前值得个数

    public int[] findMode(TreeNode<Integer> root) {
        dfs(root);
        int[] res = new int[maxList2.size()];
        for (int i = 0; i < maxList2.size(); i++) {
            res[i] = maxList2.get(i);
        }
        return res;
    }

    private void dfs(TreeNode<Integer> root) {
        if (root == null) return;
        dfs(root.left);
        change(root.val);
        dfs(root.right);
    }

    private void change(int newVal) {
        if (newVal == curVal2) {
            curCount2++;
        } else {
            curVal2 = newVal;
            curCount2 = 1;
        }

        if (curCount2 == maxCount2) {
            maxList2.add(curVal2);
        }
        if (curCount2 > maxCount2) {
            maxList2.clear();
            maxCount2 = curCount2;
            maxList2.add(curVal2);
        }
    }

    //--------------------迭代------------------
    List<Integer> maxList = new ArrayList<>();
    int maxCount; //最大的个数
    int curVal; // 当前被统计的值
    int curCount; // 当前值得个数

    public int[] findModeIterate(TreeNode<Integer> root) {
        Stack<TreeNode<Integer>> stack = new Stack<>();
        TreeNode<Integer> cur = root;
        while (cur != null || !stack.isEmpty()) {
            if (cur != null) {
                stack.push(cur);
                cur = cur.left;
            } else {
                TreeNode<Integer> pop = stack.pop();
                update(pop.val);
                cur = pop.right;
            }
        }
        int[] res = new int[maxList.size()];
        for (int i = 0; i < maxList.size(); i++) {
            res[i] = maxList.get(i);
        }
        return res;
    }

    private void update(int newVal) {
        if (newVal == curVal) {
            curCount++;
        } else {
            curVal = newVal;
            curCount = 1;
        }

        if (curCount == maxCount) {
            maxList.add(curVal);
        }
        if (curCount > maxCount) {
            maxCount = curCount;
            maxList.clear();
            maxList.add(curVal);
        }
    }


    // ------------- morris 遍历----------------
    List<Integer> maxValCountList = new ArrayList<>();
    int preVal, maxValCount, preCount;

    public int[] findModeMorris(TreeNode<Integer> root) {
        if (root == null) {
            return new int[0];
        }
        TreeNode<Integer> cur = root;
        while (cur != null) {
            if (cur.left == null) {
                updateVal(cur.val);
                cur = cur.right;
            } else {
                TreeNode mostRight = cur.left;
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) {
                    mostRight.right = cur;
                    cur = cur.left;
                } else {
                    updateVal(cur.val);
                    mostRight.right = null;
                    cur = cur.right;
                }
            }
        }
        int[] res = new int[maxValCountList.size()];
        for (int i = 0; i < maxValCountList.size(); i++) {
            res[i] = maxValCountList.get(i);
        }
        return res;
    }

    private void updateVal(int newVal) {
        if (newVal == preVal) {
            preCount++;
        } else {
            preCount = 1;
            preVal = newVal;
        }

        if (preCount == maxValCount) {
            maxValCountList.add(newVal);
        } else if (preCount > maxValCount) {
            maxValCount = preCount;
            maxValCountList.clear();
            maxValCountList.add(newVal);
        }
    }

}