package 栈和队列;

import java.util.Stack;

class 逆波兰表达式求值_150 {
    public static void main(String[] args) {
        String[] tokens = new String[]{"2", "1", "+", "3", "*"};
        System.out.println(new 逆波兰表达式求值_150().evalRPN(tokens));
    }

    public int evalRPN(String[] tokens) {
        Stack<Integer> stack = new Stack<>();
        for (String s : tokens) {
            if ("+".equals(s) || "-".equals(s) || "*".equals(s) || "/".equals(s)) {
                int num2 = stack.pop();
                int num1 = stack.pop();
                int res = 0;
                switch (s) {
                    case "+":
                        res = num1 + num2;
                        break;
                    case "-":
                        res = num1 - num2;
                        break;
                    case "*":
                        res = num1 * num2;
                        break;
                    case "/":
                        res = num1 / num2;
                        break;
                }
                stack.push(res);
            } else {
                stack.push(Integer.parseInt(s));
            }
        }
        return stack.pop();
    }
}