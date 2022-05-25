package 栈和队列;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 20. 有效的括号
 * 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串 s ，判断字符串是否有效。
 * <p>
 * 有效字符串需满足：
 * <p>
 * 左括号必须用相同类型的右括号闭合。
 * 左括号必须以正确的顺序闭合。
 */
public class 有效的括号_020 {

    public static void main(String[] args) {
        boolean valid = new 有效的括号_020().isValid("(]");
        System.out.println(valid);
    }

    public boolean isValid(String s) {
        if (s.length() % 2 == 1) {
            return false;
        }
        char[] chars = s.toCharArray();
        Stack<Character> stack = new Stack<>();
        for (char c : chars) {
            if (c == '[' || c == '{' || c == '(') {
                stack.push(c);
            } else {
                if (stack.empty()) {
                    return false;
                }
                char last = stack.pop();
                switch (c) {
                    case ']':
                        if (last != '[') {
                            return false;
                        }
                        break;
                    case '}':
                        if (last != '{') {
                            return false;
                        }
                        break;
                    case ')':
                        if (last != '(') {
                            return false;
                        }
                        break;
                }
            }
        }
        return stack.empty();
    }
}
