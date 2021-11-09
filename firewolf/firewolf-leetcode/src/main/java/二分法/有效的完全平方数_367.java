package 二分法;

/**
 * 367. 有效的完全平方数
 * 给定一个 正整数 num ，编写一个函数，如果 num 是一个完全平方数，则返回 true ，否则返回 false 。
 * <p>
 * 进阶：不要 使用任何内置的库函数，如  sqrt 。
 */
public class 有效的完全平方数_367 {

    public static void main(String[] args) {
        boolean perfectSquare = new 有效的完全平方数_367().isPerfectSquare(4);
        System.out.println(perfectSquare);
    }

    public boolean isPerfectSquare(int num) {
        int left = 0;
        int right = num;
        while (left <= right) {
            int mid = (left + right) >>> 1;
            long tmp = (long) mid * mid;
            if (num == tmp) {
                return true;
            } else if (tmp < num) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return false;
    }
}
