package 树.二叉搜索树;

import static utils.TreeUtils.*;

class 将有序数组转换为二叉搜索树_108 {

    public static void main(String[] args) {
        TreeNode root = new 将有序数组转换为二叉搜索树_108().sortedArrayToBST(new int[]{-10, -3, 0, 5, 9});
        System.out.println(showBinaryTreeByLevelWithNull(root));
    }

    public TreeNode sortedArrayToBST(int[] nums) {
        return buidSubTree(nums, 0, nums.length - 1);
    }

    private TreeNode buidSubTree(int[] nums, int start, int end) {
        if (end < start) return null;
        int mid = (start + end) / 2;
        TreeNode root = new TreeNode(nums[mid]);
        root.left = buidSubTree(nums, start, mid - 1);
        root.right = buidSubTree(nums, mid + 1, end);
        return root;
    }
}