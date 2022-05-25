package 其他;

import java.util.ArrayList;
import java.util.List;

public class 两数相除_029 {

    public static void main(String[] args) {
        int divide = new 两数相除_029().divide(-2147483648, 2);
        System.out.println(divide);
    }


    public int divide(int dividend, int divisor) {
        if (divisor == 1) {
            return dividend;
        }
        if (divisor == -1) {
            if (dividend == Integer.MIN_VALUE) {
                return Integer.MAX_VALUE;
            }
            return -dividend;
        }

        // 记录正负
        int flag = ((dividend > 0 && divisor > 0) || (dividend < 0 && divisor < 0)) ? 1 : -1;


        // 统一成负数进行处理
        if (dividend > 0) {
            dividend = -dividend;
        }
        if (divisor > 0) {
            divisor = -divisor;
        }
        if (dividend > divisor) {
            return 0;
        }

        int res = 0;
        int sub = divisor;
        List<Integer> s = new ArrayList<>();
        s.add(sub);
        while (sub >= dividend - sub) { //sub + sub >= dividend
            sub += sub;
            s.add(sub);
        }
        for (int i = s.size() - 1; i >= 0; i--) {
            if (dividend <= s.get(i)) {
                res += (1 << i);
                dividend -= s.get(i);
            }
        }
        return res * flag;
    }

    /**
     * 暴力解法
     *
     * @param dividend
     * @param divisor
     * @return
     */
    public int forceDivide(int dividend, int divisor) {
        if (dividend == Integer.MIN_VALUE && divisor == -1) {
            return Integer.MAX_VALUE;
        }

        // 记录正负
        int flag = ((dividend > 0 && divisor > 0) || (dividend < 0 && divisor < 0)) ? 1 : -1;
        int res = 0;

        // 统一成负数进行处理
        if (dividend > 0) {
            dividend = -dividend;
        }
        if (divisor > 0) {
            divisor = -divisor;
        }

        while (dividend <= divisor) { //一直减小
            dividend -= divisor;
            res++;
        }
        return res * flag;
    }
}
