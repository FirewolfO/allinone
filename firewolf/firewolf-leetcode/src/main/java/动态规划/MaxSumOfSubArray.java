package 动态规划;

/**
 * 剑指 Offer 42. 连续子数组的最大和
 * 描述：https://leetcode-cn.com/problems/lian-xu-zi-shu-zu-de-zui-da-he-lcof/
 * Author：liuxing
 * 到第n个元素的时候，最大和和第n个元素有如下关系：
 * f(n) = a[n]; 当 f(n-1) < 0;
 * f(n) = f(n-1) + a[n] ; 当f(n-1)>=0
 * f(n) = a[n]; 当 n = 1的时候
 * <p>
 * Date：2021-03-10
 */
public class MaxSumOfSubArray {

    public static void main(String[] args) {
        int[] nums = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        int res = new MaxSumOfSubArray().maxSubArray(nums);
        System.out.println(res);
    }

    public int maxSubArray(int[] a) {
        int max = a[0];
        for (int i = 1; i < a.length; i++) {
            if (a[i - 1] < 0) {
                a[i] = a[i];
            } else {
                a[i] = a[i] + a[i - 1];
            }
            if (a[i] >= max) {
                max = a[i];
            }
        }
        return max;
    }
}
