package 动态规划;

class 斐波那契数_509 {

    public static void main(String[] args) {
        System.out.println(new 斐波那契数_509().fib(2));
    }


    /*******************************优化O(1) 空间复杂度*****************************/
    public int fib(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        int fibN_2 = 0;
        int fibN_1 = 1;
        int tmp;
        for (int i = 2; i <= n; i++) {
            tmp = fibN_1;
            fibN_1 = fibN_1 + fibN_2;  // 状态转移方程
            fibN_2 = tmp;
        }
        return fibN_1;
    }

    /***********************标准动态规划****************/
    public int fib2(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        int[] dp = new int[n + 1];
        dp[0] = 0;
        dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2]; // 状态转移方程
        }
        return dp[n];
    }

}