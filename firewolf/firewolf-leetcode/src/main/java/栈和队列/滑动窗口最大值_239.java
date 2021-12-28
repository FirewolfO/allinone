package 栈和队列;

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
        for (int arrayIndex = 0; arrayIndex < nums.length; arrayIndex++) {
            while (!q.isEmpty() && nums[arrayIndex] >= nums[q.getLast()]) { // 比当前值小的元素，是不可能成为当前窗口最大值的，所以移除掉
                q.removeLast();
            }
            q.addLast(arrayIndex);
            while (q.getFirst() <= arrayIndex - k) {  // 元素的位置超出了滑动窗口的下标范围 [arrayIndex-k+1,arrayIndex ]，无效最大值，移除掉
                q.removeFirst();
            }
            if (arrayIndex >= k - 1) {
                res[resIndex++] = nums[q.getFirst()];
            }
        }
        return res;
    }

    //--------------------- 封装函数解决问题----------------------
    LinkedList<Integer> list = new LinkedList();
    public int[] maxSlidingWindow2(int[] nums, int k) {
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
    //第一个就是最大值
    private int getMax() {
        return list.getFirst();
    }
    // 如果队列中有比要加入的数据小的，先删除掉
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
    // 移除元素，只有当要移除的数据正好是最大值的时候，才需要移除
    private void remove(int num) {
        if (list.getFirst() == num) {
            list.removeFirst();
        }
    }

}
