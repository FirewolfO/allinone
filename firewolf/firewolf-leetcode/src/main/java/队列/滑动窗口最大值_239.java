package 队列;

import java.util.LinkedList;

public class 滑动窗口最大值_239 {

    public int[] maxSlidingWindow(int[] nums, int k) {
        LinkedList q = new LinkedList();
        int[] res;
        if (nums.length <= k) {
            res = new int[1];
        } else {
            res = new int[nums.length - k + 1];
        }

        int resIndex = 0;
        int arrayIndex = 0;
        while (arrayIndex < k && arrayIndex < nums.length) {
            q.addLast(nums[arrayIndex]);
        }

        return res;

    }

}
