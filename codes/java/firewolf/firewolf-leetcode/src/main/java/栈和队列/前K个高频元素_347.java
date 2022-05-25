package 栈和队列;

import java.util.*;

public class 前K个高频元素_347 {

    public static void main(String[] args) {
        int[] nums = new int[]{1, 1, 1, 2, 2, 3};
        int[] ints = new 前K个高频元素_347().topKFrequent(nums, 2);
        System.out.println(Arrays.toString(ints));
    }

    //-------------------优先级队列-----------------
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> countMap = new HashMap<>();
        for (int i : nums) {
            countMap.put(i, countMap.getOrDefault(i, 0) + 1);
        }
        PriorityQueue<Map.Entry<Integer, Integer>> priorityQueue = new PriorityQueue<>((o1, o2) -> o2.getValue() - o1.getValue());
        Iterator<Map.Entry<Integer, Integer>> iterator = countMap.entrySet().iterator();
        while (iterator.hasNext()) {
            priorityQueue.offer(iterator.next());
        }
        int[] res = new int[k];
        int index = 0;
        while (index < k) {
            res[index++] = priorityQueue.poll().getKey();
        }
        return res;

    }

    // ---------------- 普通栈--------------------------
    Stack<NumInfo> stack1 = new Stack<>();
    Stack<NumInfo> stack2 = new Stack<>();

    public int[] topKFrequent2(int[] nums, int k) {
        Arrays.sort(nums);
        int count = 1;
        int lastNum = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] != lastNum) {
                pushNum(lastNum, count);
                lastNum = nums[i];
                count = 1;
            } else {
                count++;
            }
        }
        pushNum(lastNum, count);
        while (!stack2.isEmpty()) {
            stack1.push(stack2.pop());
        }
        int[] res = new int[k];
        int index = 0;
        while (!stack1.isEmpty() && index < k) {
            res[index++] = stack1.pop().num;
        }
        return res;
    }

    private void pushNum(int lastNum, int count) {
        NumInfo numInfo = new NumInfo(lastNum, count);
        while (!stack1.isEmpty() && stack1.peek().count > numInfo.count) {
            stack2.push(stack1.pop());
        }
        while (!stack2.isEmpty() && stack2.peek().count < numInfo.count) {
            stack1.push(stack2.pop());
        }
        stack1.push(numInfo);
    }

    class NumInfo {
        public int num;
        public int count;

        public NumInfo(int num, int count) {
            this.num = num;
            this.count = count;
        }

    }

}
