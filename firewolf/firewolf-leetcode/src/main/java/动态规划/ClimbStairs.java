package 动态规划;

/**
 * 70 爬楼梯：
 * 描述：https://leetcode-cn.com/problems/climbing-stairs/
 * 解决思路：https://www.cnblogs.com/cthon/p/9251909.html
 * 1. 因为一次最多走一步或者二步，所以，到第n个台阶，一定是从第n-1个或者是第n-2个走上来的；
 * 2. 不管0步到第n-1步的走法，也不管0步到第n-2步的走法，第n步必然是从第n-1或者是第n-2走上来的
 * 3. 假设用f(n)表示0到第n步的走法，那么0到第n-1步的走法为f(n-1)，0到第n-2步的走法为f(n-2)，而f(n) = f(n-1) + f(n-2)
 * 4. 当台阶只有一个的时候，只有一种走法， f(1) = 1, 只有两个的时候，有两种走法，f(2) = 2;
 *
 * Author：liuxing
 * Date：2021-03-09
 */
public class ClimbStairs {

    public int climbStairs(int n) {
        if (n == 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }
        int a = 1;
        int b = 2;
        int tmp = 0;
        for (int i = 3; i <= n; i++) {
            tmp = b;
            b = a + b;
            a = tmp;
        }
        return b;
    }

    public static void main(String[] args) {
        System.out.println(new ClimbStairs().climbStairs(45));
    }
}
