/**
 * 描述：剑指 Offer 04. 二维数组中的查找
 * Author：liuxing
 * Date：2021-01-25
 */
public class TwoVArraySearch {

    public static void main(String[] args) {
        int aa[][] = {{}};
        System.out.println(new TwoVArraySearch().findNumberIn2DArray(aa, 5));
    }

    public boolean findNumberIn2DArray(int[][] matrix, int target) {
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i].length == 0 || matrix[i][matrix[i].length - 1] < target) {
                continue;
            }
            if (matrix[i][0] > target) {
                return false;
            }
            if (search(target, 0, matrix[i].length - 1, matrix[i])) {
                return true;
            }
        }
        return false;
    }


    private boolean search(int target, int start, int end, int[] nums) {
        if (start > end) {
            return false;
        }
        int middle = (start + end) / 2;
        if (nums[middle] == target) {
            return true;
        }
        if (target > nums[middle]) {
            return search(target, middle + 1, end, nums);
        }
        return search(target, start, middle - 1, nums);
    }
}
