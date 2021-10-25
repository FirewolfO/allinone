package 队列;

import java.util.Arrays;
import java.util.LinkedList;

public class 滑动窗口最大值_239 {

    public static void main(String[] args) {
        int[] ints = new 滑动窗口最大值_239().maxSlidingWindow(new int[]{1}, 1);
        System.out.println(Arrays.toString(ints));
    }

    public int[] maxSlidingWindow(int[] nums, int k) {
        LinkedList<Integer> q = new LinkedList();
        int[] res;
        if (nums.length <= k) { //题目有说明 1<= k <= nums.length，所以这个判断其实可以不要
            res = new int[1];
        } else {
            res = new int[nums.length - k + 1];
        }

        int resIndex = 0;
        for (int arrayIndex =0; arrayIndex < nums.length; arrayIndex++) {
            while(!q.isEmpty() && nums[arrayIndex] >= nums[q.getLast()]){ // 比当前值小的元素，是不可能成为当前窗口最大值的，所以移除掉
                q.removeLast();
            }
            q.addLast(arrayIndex);
            while(q.getFirst() <= arrayIndex - k){  // 元素的位置超出了滑动窗口的下标范围 [arrayIndex-k+1,arrayIndex ]，无效最大值，移除掉
                q.removeFirst();
            }
            if(arrayIndex >= k-1) {
                res[resIndex++] = nums[q.getFirst()];
            }
        }
        return res;
    }

}
