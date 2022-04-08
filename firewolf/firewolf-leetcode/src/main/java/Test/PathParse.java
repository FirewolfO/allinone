package Test;

/***
 * 给你一个字符串 path ，表示指向某一文件或目录的 Unix 风格 绝对路径 （以 '/' 开头），请你将其转化为更加简洁的规范路径。
 *
 * 一个点（.）表示当前目录本身；此外，两个点 （..） 表示将目录切换到上一级（指向父目录）；两者都可以是复杂相对路径的组成部分。任意多个连续的斜杠（即，'//'）都被视为单个斜杠 '/' 。 对于此问题，任何其他格式的点（例如，'...'）均被视为文件/目录名称
 *
 * 示例 1：
 * 输入：path = "/home/"
 * 输出："/home"
 * 解释：注意，最后一个目录名后面没有斜杠。 
 * 示例 2：
 * 输入：path = "/../"
 * 输出："/"
 * 解释：从根目录向上一级是不可行的，因为根目录是你可以到达的最高级。
 * 示例 3：
 * 输入：path = "/home//foo/"
 * 输出："/home/foo"
 * 解释：在规范路径中，多个连续斜杠需要用一个斜杠替换。
 * 示例 4：
 * 输入：path = "/a/./b/../../c/"
 * 输出："/c"
 */
public class PathParse {

    public static void main(String[] args) {
        int divide = new PathParse().divide(10, 3);
        System.out.println(divide);
    }


    public int divide(int a, int b) {
        if (b == 0) return 0;
        int flag = ((a >> 31) & (b >> 31)) == 0 ? 1 : -1;
        long x = a < 0 ? -a : a;
        long y = b < 0 ? -b : b;
        long left = 0;
        long right = x;
        long res = 0;
        while (left < right) {
            long mid = (left + right + 1) >> 1;
            if (mult(mid, y) < x) {
                left = mid + 1;
                res = mid;
            } else {
                right = mid - 1;
            }
        }
        res = res * flag;
        return (int) ((res > Integer.MAX_VALUE || res < Integer.MIN_VALUE) ? Integer.MAX_VALUE : res);
    }

    private long mult(long a, long k) {
        long ans = 0;
        while (k > 0) {
            if ((k & 1) == 1) ans += a;
            k = k >> 1;
            a += a;
        }
        return ans;
    }
}
