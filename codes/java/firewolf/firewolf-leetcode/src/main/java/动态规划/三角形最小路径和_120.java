package 动态规划;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class 三角形最小路径和_120 {

    public static void main(String[] args) {
        List<List<Integer>> triangle = new ArrayList<>();
        triangle.add(Arrays.asList(2));
        triangle.add(Arrays.asList(3, 4));
        triangle.add(Arrays.asList(6, 5, 7));
        triangle.add(Arrays.asList(4, 1, 8, 3));
        System.out.println(new 三角形最小路径和_120().minimumTotal(triangle));
    }

    public int minimumTotal(List<List<Integer>> triangle) {
        int size = triangle.size();
        int dp[][] = new int[size][];
        dp[0] = new int[1];
        dp[0][0] = triangle.get(0).get(0);
        for (int i = 1; i < size; i++) {
            dp[i] = new int[i + 1]; // 每一层增加一个元素
            for (int j = 0; j < triangle.get(i).size(); j++) {
                //如果正位于当前行的下标 i ，那么下一步可以移动到下一行的下标 i 或 i + 1
                // 也就是说， i, j位置的，可以从 i-1,j-1 或者是  i-1,j  到
                int lastLevelMin = 0;
                if (j == 0) {
                    lastLevelMin = dp[i - 1][j];
                } else if (j == triangle.get(i).size() - 1) {
                    lastLevelMin = dp[i - 1][j - 1];
                } else {
                    lastLevelMin = Math.min(dp[i - 1][j], dp[i - 1][j - 1]);
                }
                dp[i][j] = lastLevelMin + triangle.get(i).get(j);
            }
        }
        int res = Integer.MAX_VALUE;
        for (int i = 0; i < dp[size - 1].length; i++) {
            res = Math.min(res, dp[size - 1][i]);
        }
        return res;
    }


}