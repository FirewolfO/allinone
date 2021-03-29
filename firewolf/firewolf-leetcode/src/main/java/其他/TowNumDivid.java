package 其他;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2021/3/24 5:28 下午
 */
public class TowNumDivid {

    public int divide(int dividend, int divisor) {
        if (dividend == Integer.MIN_VALUE && divisor == -1) {
            return Integer.MAX_VALUE;
        }
        boolean sign = (dividend > 0 && divisor > 0) || (dividend < 0 && divisor < 0);
        int fDividend = dividend > 0 ? -dividend : dividend;
        int fDivisor = divisor > 0 ? -divisor : divisor;
        int res = 0;
        while (fDividend <= fDivisor) {
            int count = 1;
            int tmp = fDivisor;
            while (fDividend - tmp <= tmp) {
                tmp = tmp << 1;
                count = count << 1;
            }
            fDividend -= tmp;
            res += count;
        }
        return sign ? res : -res;
    }

    public static void main(String[] args) {
        System.out.println(new TowNumDivid().divide(10, 3));
    }
}
