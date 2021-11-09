package 指针.快慢指针;

import java.util.Arrays;

public class 移动零_283 {

    public static void main(String[] args) {
        int[] nums = new int[]{0, 1, 0, 3, 12};
        new 移动零_283().moveZeroes(nums);
        System.out.println(Arrays.toString(nums));

    }

    public void moveZeroes(int[] nums) {
        if (nums.length <= 1) {
            return;
        }
        int slow = 0;
        int fast = 0;
        while (fast < nums.length) {
            if (nums[fast] != 0) {
                nums[slow] = nums[fast];
                slow++;
            }
            fast++;
        }
        while (slow < nums.length) {
            nums[slow++] = 0;
        }
    }


}
