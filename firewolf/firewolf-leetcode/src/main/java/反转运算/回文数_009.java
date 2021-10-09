package 反转运算;

/**
 * 9. 回文数
 * 给你一个整数 x ，如果 x 是一个回文整数，返回 true ；否则，返回 false 。
 * 回文数是指正序（从左向右）和倒序（从右向左）读都是一样的整数。例如，121 是回文，而 123 不是。
 */
public class 回文数_009 {

    public static void main(String[] args) {

        System.out.println(new 回文数_009().isPalindrome(1234));
    }


    public boolean isPalindrome(int x) {
        // 根据题意，负数都不是回文数
        // 以0结尾的数字不是回文数，因为不会有0开头的数字
        if (x < 0 || (x % 10 == 0 && x != 0))
            return false;
        int res = 0;
        while (x > res) { // 只需要反转一半
            res = res * 10 + x % 10;
            x /= 10;
        }
        // 偶数位数的，反转的一半和剩余的一样 || 奇数位数的，那么反转后res比x多一位，缩小十倍应该正好一样（res最后一位是中间那个数字）
        return res == x || res / 10 == x;
    }
}
