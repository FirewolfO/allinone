package 滑动窗口;

import java.util.LinkedList;

class Solution {


    public static void main(String[] args) {
        int[] nums = new int[]{1, 3, -1, -3, 5, 3, 6, 7};
        int[] x = new Solution().maxSlidingWindow(nums, 3);
        System.out.println(x);
    }

    LinkedList<Integer> list = new LinkedList<>();

    public int[] maxSlidingWindow(int[] nums, int k) {
        int[] res = new int[nums.length - k + 1];
        for (int i = 0; i < k; i++) {
            add(nums[i]);
        }
        res[0] = getMax();
        for (int i = k; i < nums.length; i++) {
            add(nums[i]); // 添加元素
            remove(nums[i - k]); //移除前面那个元素
            res[i - k + 1] = getMax();
        }
        return res;
    }

    private int getMax() {
        return list.getFirst();
    }

    private void add(int num) {
        while (list.size() > 0) {
            int last = list.getLast();
            if (last < num) {
                list.removeLast();
            } else {
                break;
            }
        }
        list.addLast(num);
    }

    private void remove(int num) {
        if (list.getFirst() == num) {
            list.removeFirst();
        }
    }
}