package 反转运算;

/**
 * 7. 整数反转
 * 给你一个 32 位的有符号整数 x ，返回将 x 中的数字部分反转后的结果。
 *
 * 如果反转后整数超过 32 位的有符号整数的范围 [−231,  231 − 1] ，就返回 0。
 *
 * 假设环境不允许存储 64 位整数（有符号或无符号）
 */
public class 整数反转_007 {

    public static void main(String[] args) {

        System.out.println(new 整数反转_007().reverse(-12345));

    }

    public int reverse(int x) {

        int res = 0;
        while (x != 0) {
            int a = x % 10;
            if (a >= 0) {
                if (res > Integer.MAX_VALUE || res > (Integer.MAX_VALUE - a) / 10) {
                    return 0;
                } else {
                    res = res * 10 + a;
                }
            } else {
                if (res < Integer.MIN_VALUE || res < (Integer.MIN_VALUE - a) / 10) {
                    return 0;
                } else {
                    res = res * 10 + a;
                }
            }
            x = x / 10;
        }

        return res;
    }
}
