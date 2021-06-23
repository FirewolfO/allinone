package 其他;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2021/6/23 7:28 下午
 */
public class Test {

    public static void main(String[] args) {

        System.out.println(new Test().reverse(12345));

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
