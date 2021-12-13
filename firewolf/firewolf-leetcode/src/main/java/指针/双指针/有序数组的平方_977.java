package 指针.双指针;

import java.util.Arrays;

public class 有序数组的平方_977 {

    public static void main(String[] args) {
        int[] nums = new int[]{16, 1, 0, 9, 100};
        int[] ints = new 有序数组的平方_977().sortedSquares(nums);
        System.out.println(Arrays.toString(ints));
    }

    /***
     * 从两端遍历，取大的存入结果，然后移动指针
     * @param nums
     * @return
     */
    public int[] sortedSquares(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        int index = nums.length - 1;
        int[] res = new int[nums.length];
        while (index >= 0) {
            int leftS = nums[left] * nums[left];
            int rightS = nums[right] * nums[right];
            if (leftS <= rightS) {
                res[index--] = rightS;
                right--;
            } else {
                res[index--] = leftS;
                left++;
            }
        }
        return res;
    }
}
