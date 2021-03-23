package 回溯;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 17. 电话号码的字母组合
 * 连接: https://leetcode-cn.com/problems/letter-combinations-of-a-phone-number/
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2021/3/22 6:13 下午
 */
public class PhoneNum {

    char[][] cchars = {{'a', 'b', 'c'}, {'d', 'e', 'f'}, {'g', 'h', 'i'}, {'j', 'k', 'l'},
            {'m', 'n', 'o'}, {'p', 'q', 'r', 's'}, {'t', 'u', 'v'}, {'w', 'x', 'y', 'z'}
    };

    public List<String> letterCombinations(String digits) {
        List<String> res = new ArrayList<>();
        if (digits.length() == 0) {
            return res;
        }

        char[] digitsChars = digits.toCharArray();
        find(res, digitsChars, 0, "");

        return res;
    }

    private void find(List<String> res, char[] digitsChars, int index, String oneStr) {
        if (digitsChars.length == index) {
            res.add(oneStr);
            return;
        }
        char c = digitsChars[index];
        char[] ms = cchars[c - '2'];
        for (int i = 0; i < ms.length; i++) {
            find(res, digitsChars, index + 1, oneStr + ms[i]);
        }
    }

    public static void main(String[] args) {

        System.out.println(new PhoneNum().letterCombinations("23"));
    }
}
