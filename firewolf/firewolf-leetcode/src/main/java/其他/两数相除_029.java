package 其他;

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
        long x = dividend;
        long y = divisor;
        while (x <= y) { //一直减小
            int c = 1;
            long a = y;
            while (true) {
                long tmp = a + a;
                if (tmp < x) {
                    break;
                }
                a = tmp;
                c += c;
            }
            x -= a;
            res += c;
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
