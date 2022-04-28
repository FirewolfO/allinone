import utils.ArrayUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static utils.LinkUtil.*;

class Solution {

    public static void main(String[] args) {
        int[][] ints = ArrayUtils.to2Array("[[1,4,7,11,15],[2,5,8,12,19],[3,6,9,16,22],[10,13,14,17,24],[18,21,23,26,30]]", ",");
        boolean b = new Solution().searchMatrix(ints, 5);
        System.out.println(b);
    }

    public boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length;
        int n = matrix[0].length;
        for (int row = 0; row < m; row++) {
            int left = 0;
            int right = n - 1;
            while (left <= right) {
                int mid = (left + right + 1) >> 1;
                if (matrix[row][mid] == target) return true;
                if (matrix[row][mid] > target) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
        }
        return false;
    }

}