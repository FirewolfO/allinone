package 动态规划;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2021/6/27 5:44 下午
 */
public class 最长回文串 {

    public static void main(String[] args) {

        System.out.println(new 最长回文串().longestPalindrome("babad"));
    }

    public String longestPalindrome(String s) {
        boolean dp[][] = new boolean[s.length()][s.length()];
        for (int i = 0; i < s.length(); i++) {
            dp[i][i] = true;
        }

        int maxLen = 1;
        int start = 0;
        int end = 0;
        for (int i = s.length() - 2; i >= 0; i--) {
            for (int j = i + 1; j < s.length(); j++) {
                if (j - i <= 1) {
                    dp[i][j] = s.charAt(i) == s.charAt(j);
                } else {
                    dp[i][j] = dp[i + 1][j - 1] && (s.charAt(i) == s.charAt(j));
                }
                if (dp[i][j] == true && (j - i + 1) > maxLen) {
                    maxLen = j - i + 1;
                    start = i;
                    end = j;
                }
            }
        }
        return s.substring(start, end + 1);
    }
}

