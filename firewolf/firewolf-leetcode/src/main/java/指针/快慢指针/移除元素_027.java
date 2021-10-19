package 指针.快慢指针;

/**
 * 27. 移除元素
 * 给你一个数组 nums 和一个值 val，你需要 原地 移除所有数值等于 val 的元素，并返回移除后数组的新长度。
 * <p>
 * 不要使用额外的数组空间，你必须仅使用 O(1) 额外空间并 原地 修改输入数组。
 * <p>
 * 元素的顺序可以改变。你不需要考虑数组中超出新长度后面的元素。
 */
public class 移除元素_027 {

    public static void main(String[] args) {
        int i = new 移除元素_027().removeElement(new int[]{2, 1, 2, 1}, 2);
        System.out.println(i);
    }

    public int removeElement(int[] nums, int val) {
        if (nums.length == 0) {
            return 0;
        }
        int slow = -1;
        int fast = 0;
        while (fast < nums.length) {
            if (nums[fast] != val) {
                nums[++slow] = nums[fast];
            }
            fast++;
        }
        return slow + 1;
    }
}
