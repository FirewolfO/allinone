package 栈和队列;

import java.util.LinkedList;

class 用队列实现栈_225 {

    private LinkedList<Integer> q1;
    private LinkedList<Integer> q2;

    public 用队列实现栈_225() {
        q1 = new LinkedList<>();
        q2 = new LinkedList<>();
    }

    public void push(int x) {
        q1.add(x);
    }

    public int pop() {
        return dumpQ1(true);
    }

    public int top() {
        return dumpQ1(false);
    }

    public boolean empty() {
        return q1.isEmpty();
    }

    private int dumpQ1(boolean remove) {
        while (q1.size() > 1) {
            q2.add(q1.pop());
        }
        int res = q1.pop();
        while (!q2.isEmpty()) {
            q1.add(q2.pop());
        }
        if (!remove) {
            q1.add(res);
        }
        return res;
    }
}