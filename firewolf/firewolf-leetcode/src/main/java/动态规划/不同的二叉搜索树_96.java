package 动态规划;

class 不同的二叉搜索树_96 {

    public static void main(String[] args) {
        System.out.println(new 不同的二叉搜索树_96().numTrees(3));
    }

    public int numTrees(int n) {
        if (n <= 2) return n;
        int[] dp = new int[n + 1];
        dp[0] = dp[1] = 1;
        dp[2] = 2;
        for (int i = 3; i <= n; i++) {
            for (int j = 1; j <= i; j++) {
                //以j为根节点，左子树的节点个数为j-1，所以，左子树的个数为dp[j-1];
                //右子树的节点个数为 i-j，所以右子树的个数为dp[i-j]，总数量为 左子树个数*右子树个数
                dp[i] += dp[j - 1] * dp[i - j];
            }
        }
        return dp[n];
    }
}