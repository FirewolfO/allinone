package 二分法;

/**
 * 描述：35. 搜索插入位置
 * 连接：https://leetcode-cn.com/problems/search-insert-position/
 */
class InsertPosition {

    public static void main(String[] args) {
        int[] nums = {1, 3, 3, 5, 6};
        int target = 4;
        System.out.println(new InsertPosition().searchInsert(nums, target));
    }

    public int searchInsert(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int middle = (left + right) / 2;
            if (nums[middle] < target) {
                left = middle + 1;
            } else if (nums[middle] >= target) {
                right = middle - 1;
            }
        }
        return left;
    }


}