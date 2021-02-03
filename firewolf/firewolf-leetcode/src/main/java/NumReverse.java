/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2021/2/3 9:51 下午
 */
public class NumReverse {

    public static void main(String[] args) {
        System.out.println(new NumReverse().reverse(1534236469));
    }

    public int reverse(int x) {
        int result = 0;
        while (x != 0) {
            int next = x % 10;
            if (result >= Integer.MAX_VALUE / 10 - next || result <= Integer.MIN_VALUE / 10 - next) {
                return 0;
            }
            result = result * 10 + next;
            x = x / 10;
        }
        return result;
    }
}
