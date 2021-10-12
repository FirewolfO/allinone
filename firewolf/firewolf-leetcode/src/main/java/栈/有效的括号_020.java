package 栈;

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
        Stack<Character> stack = new Stack<>();
        char[] chars = s.toCharArray();
        Map<Character, Character> map = new HashMap<>();
        map.put(']', '[');
        map.put('}', '{');
        map.put(')', '(');
        int i = 0;
        while (i < chars.length) {
            char c = chars[i];
            if (map.containsKey(c)) {
                if (stack.empty()) {
                    return false;
                }
                Character peek = stack.peek();
                if (peek.charValue() == map.get(c)) {
                    stack.pop();
                } else {
                    stack.push(c);
                }
            } else {
                stack.push(c);
            }
            i++;
        }
        return stack.empty();
    }
}
