package 滑动窗口;

import java.util.HashMap;
import java.util.Map;

public class 水果成篮_904 {

    public static void main(String[] args) {
        int[] nums = new int[]{3, 3, 3, 1, 2, 1, 1, 2, 3, 3, 4};
        int i = new 水果成篮_904().totalFruit(nums);
        System.out.println(i);
    }

    public int totalFruit(int[] fruits) {
        Map<Integer, Integer> win = new HashMap<Integer, Integer>();
        int left = 0;
        int right = 0;
        int max = 0;
        int totalCount = 0;
        while (right < fruits.length) {
            int count = win.containsKey(fruits[right]) ? win.get(fruits[right]) + 1 : 1;
            win.put(fruits[right], count);
            totalCount++;
            while (win.size() > 2 && left < right) { // 让保存的数据满足是2种
                int hasCount = win.get(fruits[left]);
                if (hasCount - 1 > 0) {
                    win.put(fruits[left], hasCount - 1);
                } else {
                    win.remove(fruits[left]);
                }
                left++;
                totalCount--;
            }
            max = totalCount > max ? totalCount : max;
            right++;
        }

        return max;
    }

}
