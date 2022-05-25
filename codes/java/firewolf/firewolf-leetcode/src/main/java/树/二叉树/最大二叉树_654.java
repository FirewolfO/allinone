package 树.二叉树;

import static utils.TreeUtils.*;

class 最大二叉树_654 {

    public static void main(String[] args) {
        int[] nums = new int[]{3, 2, 1, 6, 0, 5};
        System.out.println(new 最大二叉树_654().constructMaximumBinaryTree(nums));
    }

    public TreeNode constructMaximumBinaryTree(int[] nums) {
        return constructMaximumBinaryTree(nums, 0, nums.length - 1);
    }

    private TreeNode constructMaximumBinaryTree(int[] nums, int startIndex, int endIndex) {
        int maxIndex = startIndex;
        int max = nums[startIndex];
        for (int i = startIndex + 1; i <= endIndex; i++) { //查找根节点
            if (nums[i] > max) {
                max = nums[i];
                maxIndex = i;
            }
        }
        TreeNode node = new TreeNode(max); // 构造根节点
        if ((maxIndex - 1) - startIndex >= 0) { // 左子树有元素，构建左子树
            node.left = constructMaximumBinaryTree(nums, startIndex, maxIndex - 1);
        }
        if (endIndex - (maxIndex + 1) >= 0) { // 右子树有元素，构建右子树
            node.right = constructMaximumBinaryTree(nums, maxIndex + 1, endIndex);
        }
        return node;
    }
}