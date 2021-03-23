package 栈;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Description: 20. 有效的括号
 * https://leetcode-cn.com/problems/valid-parentheses/
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2021/3/23 3:20 下午
 */
public class ValidBrackets {


    public static void main(String[] args) {
        String s = "[]";
        System.out.println(new ValidBrackets().isValid(s));
    }

    public boolean isValid(String s) {
        Map<Character, Character> cMap = new HashMap<>();
        cMap.put(')', '(');
        cMap.put('}', '{');
        cMap.put(']', '[');
        if (s.length() % 2 != 0) {
            return false;
        }
        Stack<Character> stack = new Stack<>();
        char[] cChars = s.toCharArray();
        for (int i = 0; i < cChars.length; i++) {
            if (cMap.containsKey(cChars[i])) {
                if (stack.isEmpty() || !stack.peek().equals(cMap.get(cChars[i]))) {
                    return false;
                } else {
                    stack.pop();
                }
            } else {
                stack.push(cChars[i]);
            }
        }
        if (stack.isEmpty()) {
            return true;
        }
        return false;
    }
}
