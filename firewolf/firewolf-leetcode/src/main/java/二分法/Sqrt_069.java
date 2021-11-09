package 二分法;

/**
 * 69. Sqrt(x)
 * 给你一个非负整数 x ，计算并返回 x 的 算术平方根 。
 * <p>
 * 由于返回类型是整数，结果只保留 整数部分 ，小数部分将被 舍去 。
 * <p>
 * 注意：不允许使用任何内置指数函数和算符，例如 pow(x, 0.5) 或者 x ** 0.5 。
 */
public class Sqrt_069 {
    public static void main(String[] args) {
        int i = new Sqrt_069().mySqrt(6);
        System.out.println(i);
    }

    public int mySqrt(int x) {
        if (x == 0) {
            return 0;
        }
        int left = 0;
        int right = x;
        int res = -1;
        while (left < right) {
            int mid = (left + right + 1) >>> 1;
            long tmp = (long) mid * mid;
            if (tmp <= x) {
                res = mid;
                left = mid;
            } else {
                right = mid - 1;
            }
        }
        return res;
    }
}
