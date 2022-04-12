package Test;

import utils.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Solution {

    public static void main(String[] args) {
        int[][] matrix = ArrayUtils.to2Array("[[1,3,5,7],[10,11,16,20],[23,30,34,50]]", ",");
        System.out.println(new Solution().searchMatrix(matrix, 11));
    }

    public boolean searchMatrix(int[][] matrix, int target) {
        int row = findRow(matrix, target);
        if (row == -1) return false;
        return findInRow(matrix, target, row);
    }

    private int findRow(int[][] matrix, int target) {
        int low = -1;
        int high = matrix.length - 1;
        while (low < high) {
            int mid = (low + high + 1) >> 1;
            if (matrix[mid][0] <= target) {
                low = mid;
            } else {
                high = mid - 1;
            }
        }
        return low;
    }

    private boolean findInRow(int[][] matrix, int target, int row) {
        int[] nums = matrix[row];
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + right >> 1;
            if (nums[mid] == target) {
                return true;
            } else if (nums[mid] > target) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return false;
    }
}