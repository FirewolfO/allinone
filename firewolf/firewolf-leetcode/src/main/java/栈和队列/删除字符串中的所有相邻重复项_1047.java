package 栈和队列;

import java.util.Stack;

class 删除字符串中的所有相邻重复项_1047 {

    public static void main(String[] args) {
        String s = "abbaca";
        String s1 = new 删除字符串中的所有相邻重复项_1047().removeDuplicates(s);
        System.out.println(s1);
    }

    public String removeDuplicates(String s) {
        Stack<Character> stack = new Stack<>();
        char[] chars = s.toCharArray();
        for (int i = chars.length - 1; i >= 0; i--) {
            if (!stack.empty() && stack.peek() == chars[i]) {
                stack.pop();
            } else {
                stack.push(chars[i]);
            }
        }
        String res = "";
        while (!stack.empty()) {
            res += stack.pop();
        }
        return res;
    }
}