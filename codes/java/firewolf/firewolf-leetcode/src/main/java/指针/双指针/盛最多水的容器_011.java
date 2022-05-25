package 指针.双指针;

/**
 * https://leetcode-cn.com/problems/container-with-most-water/
 * 11. 盛最多水的容器
 * 给你 n 个非负整数 a1，a2，...，an，每个数代表坐标中的一个点 (i, ai) 。在坐标内画 n 条垂直线，垂直线 i 的两个端点分别为 (i, ai) 和 (i, 0) 。找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
 * <p>
 * 说明：你不能倾斜容器。
 */
public class 盛最多水的容器_011 {

    public static void main(String[] args) {
        int maxArea = new 盛最多水的容器_011().maxArea(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7});
        System.out.println(maxArea);
    }


    public int maxArea(int[] height) {
        int left = 0; // 左指针
        int lastLeft = 0; // 上一次计算出最大区域的左指针
        int right = height.length - 1; // 右指针
        int lastRight = height.length - 1; // 上一次计算出最大区域的右指针
        int maxArea = 0;

        while (left < right) {
            int area = (right - left) * Math.min(height[left], height[right]); // 高度取决于低的那个
            if (area > maxArea) { // 如果新的面积比之前的大，更新面积和最大面积时候的指针
                maxArea = area;
                lastLeft = left;
                lastRight = right;
            }
            if (height[left] < height[right]) { // 左边的矮，则左指针移动
                do {
                    left++;
                } while (left < right && height[left] <= height[lastLeft]); // 如果高度没有上次最大面积的高度高，那么面积一定会小，没有必要计算
            } else {
                do {
                    right--;
                } while (right > left && height[right] <= height[lastRight]);
            }
        }
        return maxArea;
    }
}
