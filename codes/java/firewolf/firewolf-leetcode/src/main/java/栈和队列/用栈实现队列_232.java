package 栈和队列;

import java.util.Stack;

class 用栈实现队列_232 {
    private Stack<Integer> stack1;
    private Stack<Integer> stack2;

    public 用栈实现队列_232() {
        stack1 = new Stack<>();
        stack2 = new Stack<>();
    }

    public void push(int x) {
        stack1.push(x);
    }

    public int pop() {
        dumpStack1();
        return stack2.pop();
    }

    public int peek() {
        dumpStack1();
        return stack2.peek();
    }

    private void dumpStack1() {
        if (stack2.empty()) {
            while (!stack1.empty()) {
                stack2.push(stack1.pop());
            }
        }
    }

    public boolean empty() {
        return stack1.empty() && stack2.empty();
    }
}