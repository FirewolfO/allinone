package 临界处理;

/**
 * 8. 字符串转换整数 (atoi)
 * 请你来实现一个 myAtoi(string s) 函数，使其能将字符串转换成一个 32 位有符号整数（类似 C/C++ 中的 atoi 函数）。
 * <p>
 * 函数 myAtoi(string s) 的算法如下：
 * <p>
 * 读入字符串并丢弃无用的前导空格
 * 检查下一个字符（假设还未到字符末尾）为正还是负号，读取该字符（如果有）。 确定最终结果是负数还是正数。 如果两者都不存在，则假定结果为正。
 * 读入下一个字符，直到到达下一个非数字字符或到达输入的结尾。字符串的其余部分将被忽略。
 * 将前面步骤读入的这些数字转换为整数（即，"123" -> 123， "0032" -> 32）。如果没有读入数字，则整数为 0 。必要时更改符号（从步骤 2 开始）。
 * 如果整数数超过 32 位有符号整数范围 [−231,  231 − 1] ，需要截断这个整数，使其保持在这个范围内。具体来说，小于 −231 的整数应该被固定为 −231 ，大于 231 − 1 的整数应该被固定为 231 − 1 。
 * 返回整数作为最终结果。
 */
public class 字符串转整数_008 {

    public static void main(String[] args) {
        int res = new 字符串转整数_008().myAtoi("2147483648");
        System.out.println(res);
    }

    public int myAtoi(String s) {
        if (s.length() < 1) {
            return 0;
        }
        int res = 0;
        int flag = 1;
        char[] chars = s.toCharArray();
        int i = 0;
        while (i < chars.length && chars[i] == ' ') i++; // 去掉多余的空格
        if (i > chars.length - 1) {
            return res;
        }
        if (chars[i] == '-') {
            flag = -1;
            i++;
        } else if (chars[i] == '+') {
            i++;
        }
        while (i < chars.length) {
            if (chars[i] < '0' || chars[i] > '9') {
                break;
            } else {
                int cInt = chars[i] - '0';
                // res * 10 > Integer.MAX_VALUE ，一定越界， ||  res * 10 == Integer.MAX_VALUE 的时候，如果cInt>7了，那么和也会大于Integer.MAX_VALUE
                if (flag == 1 && (res > Integer.MAX_VALUE / 10 || (res == Integer.MAX_VALUE / 10 && cInt > 7))) {
                    return Integer.MAX_VALUE;
                } else if (flag == -1 && (res * -1 < Integer.MIN_VALUE / 10 || (res * -1 == Integer.MIN_VALUE / 10 && cInt > 8))) {
                    // res * -10 < Integer.MIN_VALUE ，一定越界 || res * -10 ==  Integer.MIN_VALUE 的时候，如果cInt > 8 ，那结果就会 < -8 ,总和也会小于Integer.MIN_VALUE 导致越界
                    return Integer.MIN_VALUE;
                }
                res = res * 10 + cInt;
                i++;
            }
        }
        return res * flag;
    }

}