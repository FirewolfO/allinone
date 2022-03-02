package 动态规划;

class 一和零_474 {

    public static void main(String[] args) {
        String[] strs = {"11111", "100", "1101", "1101", "11000"};
        int maxForm = new 一和零_474().findMaxFormWithRollArray(strs, 5, 7);
        System.out.println(maxForm);
    }

    public int findMaxForm(String[] strs, int m, int n) {

        //dp[i][j][k]： strs[0] ~ strs[i] 的时候，j个0 和 k 个1的最大集合个数
        int[][][] dp = new int[strs.length][m + 1][n + 1];
        int[] str0Counts = getCounts(strs[0]);
        for (int j = str0Counts[0]; j < m + 1; j++) { // 只有第一个字符串的时候，赋值
            for (int k = str0Counts[1]; k < n + 1; k++) {
                dp[0][j][k] = 1;
            }
        }

        for (int i = 1; i < strs.length; i++) {
            for (int j = 0; j < m + 1; j++) {
                for (int k = 0; k < n + 1; k++) {
                    int[] counts = getCounts(strs[i]);
                    dp[i][j][k] = dp[i - 1][j][k];// 先把只有 0~i-1 字符串的值拷贝了
                    if (counts[0] <= j && counts[1] <= k) { // 如果可以加入第i个字符串，取最大的
                        dp[i][j][k] = Math.max(dp[i][j][k], dp[i - 1][j - counts[0]][k - counts[1]] + 1);
                    }
                }
            }
        }
        return dp[strs.length - 1][m][n];
    }


    public int findMaxFormWithRollArray(String[] strs, int m, int n) {
        //dp[i][j]：最多i个0和j个1情况下最大的子集合数
        //dp[i][j] = Math.max(dp[i][j], dp[i - strs[k]的0的数量][j - strs[k]的1的的数量] + 1);   // p[i][j] 不要当前字符串strs[k]，  dp[i - str[k]的0的数量][j - str[k]的1的的数量] + 1 表示使用了字符串strs[k]
        int[][] dp = new int[m + 1][n + 1];
        for (String str : strs) {
            int[] counts = getCounts(str);
            for (int i = m; i >= counts[0]; i--) {
                for (int j = n; j >= counts[1]; j--) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - counts[0]][j - counts[1]] + 1);
                }
            }
        }
        return dp[m][n];
    }

    private int[] getCounts(String str) {
        int[] counts = new int[2];
        for (char c : str.toCharArray()) {
            if (c == '0') {
                counts[0]++;
            } else {
                counts[1]++;
            }
        }
        return counts;
    }
}