package 滑动窗口;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2021/2/22 10:00 下午
 */
public class ArrayMaxSum {

    public static void main(String[] args) {

        int[] nums = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        System.out.println(new ArrayMaxSum().maxSubArray(nums));
    }

    public int maxSubArray(int[] nums) {
        int max = nums[0];
        int right = 0;
        int left = -1;
        int current = max;
        while (right < nums.length - 1) {
            ++right;
            current += nums[right];
            if (current > max) {
                max = current;
                while (left < right) {
                    int m = left + 1;
                    left = m;
                    current -= nums[m];
                    if (current >= max) {
                        max = current;
                        break;
                    }

                }

            }
        }
        return max;
    }
}
